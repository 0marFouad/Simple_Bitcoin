import java.io.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class PrintBlock implements Runnable {
    Block root;

    PrintBlock(Block root) {
        while (root.getParent() != null) {
            root = root.getParent();
        }
        this.root = root;
    }

    @Override
    public void run() {
        File fout = new File("chain.txt");
        try {
            FileWriter f = new FileWriter("chain.txt");
            String line = System.getProperty("line.separator");
            Queue<Block> q = new ArrayDeque<>();
            q.add(root);
            String temp = "";
            int currLevel = root.level;
            while (!q.isEmpty()) {
                Block top = q.poll();
                if (top.level > currLevel) {
                    currLevel = top.level;
                    System.out.println(temp);
                    f.write(temp + line);
                    temp = "";
                }
                List<Transaction> topTxList = top.getTxList();
                String toAdd = top.level + "(" + topTxList.get(0).getId() + ", " + topTxList.get(topTxList.size() - 1).getId() + ", " + top.getTimestamp() + ")    ";
                temp += toAdd;
                List<Block> topChildren = top.getChildren();
                System.out.println("top queue children size: " + topChildren.size());
                for (int i = 0; i < topChildren.size(); i++) {
                    q.add(topChildren.get(i));
                }

            }
            f.write(temp + line);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
