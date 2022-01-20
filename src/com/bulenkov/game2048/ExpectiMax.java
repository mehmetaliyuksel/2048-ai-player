package com.bulenkov.game2048;

public class ExpectiMax {

    // // Initializing Nodes to null
    // static Node newNode(int v)
    // {
    // Node temp = new Node();
    // temp.value = v;
    // return temp;
    // }

    // Getting expectimax
    public double expectiMax(Node node, boolean isMaxNode) {

        // Base case
        if (node.getChildren().isEmpty()) {
            return node.getValue();
        }

        if (isMaxNode) {
            return node.getChildren()
                    .stream()
                    .mapToDouble(child -> expectiMax(child, false))
                    .max()
                    .getAsDouble();
        }

        return node.getChildren()
                .stream()
                .mapToDouble(child -> child.getProbability() * expectiMax(child, true))
                .sum();
    }
}

// Driver code
// public static void main(String[] args)
// {
// // Non leaf nodes.
// // If search is limited
// // to a given depth,
// // their values are
// // taken as heuristic value.
// // But because the entire tree
// // is searched their
// // values don't matter
// Node root = newNode(0);
// root.left = newNode(0);
// root.right = newNode(0);

// // Assigning values to Leaf nodes
// root.left.left = newNode(10);
// root.left.right = newNode(10);
// root.right.left = newNode(9);
// root.right.right = newNode(100);

// float res = expectimax(root, true);
// System.out.print("Expectimax value is "
// + res +"\n");
// }
// }

// This code is contributed by PrinciRaj1992
