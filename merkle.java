import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

class MerkleTree<T> {
    public static class Node<T> {
        public String hash;
        public Node<T> left;
        public Node<T> right;

        public Node(String hash) {
            this.hash = hash;
        }

        public Node(Node<T> left, Node<T> right, ObjectHasher<T> hasher) {
            this.left = left;
            this.right = right;
            this.hash = hasher.hash((T) (left.hash + right.hash));
        }
    }

    private final ObjectHasher<T> hasher;
    public Node<T> root;

    public MerkleTree(List<T> dataBlocks, ObjectHasher<T> hasher) {
        this.hasher = hasher;
        if (dataBlocks.isEmpty()) {
            throw new IllegalArgumentException("Data blocks cannot be empty");
        }
        List<Node<T>> nodes = new ArrayList<>();
        for (T data : dataBlocks) {
            nodes.add(new Node<>(hasher.hash(data)));
        }
        while (nodes.size() > 1) {
            List<Node<T>> newNodes = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                if (i + 1 < nodes.size()) {
                    newNodes.add(new Node<>(nodes.get(i), nodes.get(i + 1), hasher));
                } else {
                    newNodes.add(nodes.get(i)); // handle odd case
                }
            }
            nodes = newNodes;
        }
        this.root = nodes.get(0);
    }
}
