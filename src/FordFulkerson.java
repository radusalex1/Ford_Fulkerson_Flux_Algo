import java.util.LinkedList;

public class FordFulkerson {
    private int nrNoduri;

    public FordFulkerson(int nrNoduri) {
        this.nrNoduri = nrNoduri;
    }

    boolean bfs(int rGraph[][],int s,int t,int parent[])
    {
        // Create a visited array and mark all vertices as
        // not visited
        boolean visited[] = new boolean[nrNoduri];
        for (int i = 0; i < nrNoduri; ++i)
            visited[i] = false;

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue
                = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        // Standard BFS Loop
        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < nrNoduri; v++) {
                if (visited[v] == false
                        && rGraph[u][v] > 0) {
                    // If we find a connection to the sink
                    // node, then there is no point in BFS
                    // anymore We just have to set its parent
                    // and can return true
                    if (v == t) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // We didn't reach sink in BFS starting from source,
        // so return false
        return false;
    }


    public int fordFulkerson(int graph[][],int s,int t)
    {
         int u,v;

        //creez graful rezidual si il umplu cu capacitatile date
        // ca si capacitati reziduale
        int rGraph[][]=new int[nrNoduri][nrNoduri];

        for (u = 0; u < nrNoduri; u++) {
            for (v = 0; v < nrNoduri; v++) {
                rGraph[u][v] = graph[u][v];
            }
        }

        //creez parentarray pentru a retine path
        int parent[] = new int[nrNoduri];

        int max_flow = 0;

        while(bfs(rGraph,s,t,parent))
        {
            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                path_flow
                        = Math.min(path_flow, rGraph[u][v]);
            }

            // update residual capacities of the edges and
            // reverse edges along the path
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }

            max_flow +=path_flow;

        }

        return max_flow;

    }


}
