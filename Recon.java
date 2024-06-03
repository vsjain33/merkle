import java.sql.SQLException;
import java.util.List;

class MerkleTreeDataReconciliation<T> implements DataReconciliation {
    private final DataFetcher<T> dataFetcher;
    private final ObjectHasher<T> hasher;

    public MerkleTreeDataReconciliation(DataFetcher<T> dataFetcher, ObjectHasher<T> hasher) {
        this.dataFetcher = dataFetcher;
        this.hasher = hasher;
    }

    @Override
    public boolean reconcileTables(String table1, String column1, String table2, String column2) throws SQLException {
        List<T> data1 = dataFetcher.fetchTableData(table1, column1);
        List<T> data2 = dataFetcher.fetchTableData(table2, column2);

        MerkleTree<T> tree1 = new MerkleTree<>(data1, hasher);
        MerkleTree<T> tree2 = new MerkleTree<>(data2, hasher);

        return tree1.root.hash.equals(tree2.root.hash);
    }
}
