//Edge class is used to store the information of an edge of the graph
//implements the Comparable interface to sort the edges in ascending order of weight
public class Edge implements Comparable<Edge>{

    //vertex 1 of an edge
    private String vertex1 = null;

    //vertex 2 of an edge
    private String vertex2 = null;

    //weight of the edge
    private int weight = 1;

    //Edge constructor is used to create an edge of the graph in addEdge method
    Edge(String vertex1, String vertex2, int weight){
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    /*
    getVertex1 method
    returns the value of vertex1 of this object
     */
    public String getVertex1() {
        return this.vertex1;
    }

    /*
    getVertex2 method
    returns the value of vertex2 of this object
    */
    public String getVertex2() {
        return this.vertex2;
    }

    /*
    getWeight method
    returns the value of weight of this object
    */
    public int getWeight() {
        return this.weight;
    }

    /*
    compareTo method
    overrided method from Comparable interface
    returns the integer value
    method is used to sort the edges in ascending order of weights
    if weights are equal compares the value of vertices
    */
    @Override
    public int compareTo(Edge edge) {
        int compare = this.weight - edge.getWeight();

        //check weight of the edges are same
        if(compare == 0){

            //when weights are same compare the vertex1 values
            compare = this.vertex1.compareTo(edge.getVertex1());

            if(compare == 0){

                //when weights and vertex1 of the edges are same
                //comparing vertex2
                compare = this.vertex2.compareTo(edge.getVertex2());

                //we can return vertex2 comparison since we handled duplicate
                return compare;
            } else {

                //returning the vertex1 value if it is not zero
                return compare;
            }

        } else {

            //returning the weight value if it is not zero
            return compare;
        }
    }

}
