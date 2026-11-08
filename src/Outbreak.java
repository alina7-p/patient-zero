import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] take = new int[n];
        int[] skip = new int[n];
        int[] parent = new int[n];
        parent[0] = -1;
        dfsCost(0, -1, nb, cost, take, skip, parent);

        boolean[] tested = new boolean[n];
        tested[0] = take[0] <= skip[0];
        rebuild(0, nb, take, skip, parent, tested);
        return tested;
    }

    private static void dfsCost(int v, int p, int[][] nb, int[] cost, int[] take, int[] skip, int[] parent) {
        parent[v] = p;
        take[v] = cost[v];
        skip[v] = 0;
        for (int c : nb[v]) {
            if (c == p) continue;
            dfsCost(c, v, nb, cost, take, skip, parent);
            take[v] += Math.min(take[c], skip[c]); // v tested: child is free
            skip[v] += take[c];                    // v not tested: child must be tested
        }
    }

    private static void rebuild(int v, int[][] nb, int[] take, int[] skip, int[] parent, boolean[] tested) {
        for (int c : nb[v]) {
            if (c == parent[v]) continue;
            tested[c] = !tested[v] || take[c] <= skip[c];
            rebuild(c, nb, take, skip, parent, tested);
        }
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] best = new int[1];
        down(0, -1, nb, best);
        return best[0];
    }

    private static int down(int v, int p, int[][] nb, int[] best) {
        int top1 = 0, top2 = 0;
        for (int c : nb[v]) {
            if (c == p) continue;
            int len = down(c, v, nb, best) + 1;
            if (len > top1) { top2 = top1; top1 = len; }
            else if (len > top2) top2 = len;
        }
        best[0] = Math.max(best[0], top1 + top2);
        return top1;
    }
}