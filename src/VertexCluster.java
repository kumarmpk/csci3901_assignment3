import java.util.*;

//VertexCluster class has the details of the graph
//all manipulations on the graph is done in this class

public class VertexCluster {

    //allEdges list contains all the edges of the graph with vertex details
    private List<Edge> allEdges = new LinkedList<>();

    //clustNoVertMap is used to store the vertices of a cluster
    //with cluster number as key and set of vertices as value
    private Map<Integer, Set<String>> clustNoVertMap = null;

    //clustNoMaxWeiMap is used to store the maximum weight of a cluster
    //with cluster number as key and maximum weight as value
    private Map<Integer, Integer> clustNoMaxWeiMap = null;

    //vertClustNoMap is used to store the vertices and its cluster numbers
    private Map<String, Integer> vertClustNoMap = null;

    //vertMaxWeiMap is used to store the vertices and its maximum weight
    private Map<String, Integer> vertMaxWeiMap = null;

    private final int ONE = 1;
    private final int ZERO = 0;

    /*
    validateString method
    validates the String input and returns boolean value
     */
    private boolean validateString(String input){
        boolean isValid = false;
        if(input != null){      //null check
            input = input.trim();
            if(!input.isEmpty()){       //empty string check
                isValid = true;
            }
        }
        return isValid;
    }

    /*
    printString method
    gets a string input and prints the message to the user
     */
    private void printString(String input){
        System.out.println(input);
    }

    /*
    duplicateCheck method
    returns boolean value meaning that the edge is already present in the graph
     */
    private boolean duplicateCheck(Edge edge){
        boolean isDuplicate = false;

        //looping all the edges
        for(Edge eachEdge : allEdges){

            //checking same edge is already in the list
            if((eachEdge.getVertex1().equals(edge.getVertex1()) && eachEdge.getVertex2().equals(edge.getVertex2()))){

                isDuplicate = true;
            }
        }

        return isDuplicate;
    }


    /*
    addEdge method
    gets vertices and weight of the edge as input
    adds to the list
    returns a boolean value
     */
    public boolean addEdge( String vertex1, String vertex2, int weight ){
        boolean isAdded = false;
        try {
            //basic validations done on inputs
            if (validateString(vertex1) && validateString(vertex2)
                    && weight != ZERO && weight > ZERO) {

                //single vertex edge check
                if (!vertex1.equals(vertex2)) {

                    String smalVertex = null;
                    String bigVertex = null;

                    int compare = vertex1.compareTo(vertex2);

                    //smaller valued vertex is set as vertex1
                    // and bigger one set as vertex2 for easy computation
                    if(compare <= ZERO){
                        smalVertex = vertex1;
                        bigVertex = vertex2;
                    } else {
                        smalVertex = vertex2;
                        bigVertex = vertex1;                    }

                    //new edge is created by calling constructor
                    Edge edge = new Edge(smalVertex, bigVertex, weight);

                    //duplicate check is done
                    if (!duplicateCheck(edge)) {

                        //new edge is added to the list
                        allEdges.add(edge);
                        isAdded = true;

                        //list is sorted in ascending order of weights, vertex1 and vertex2
                        //this is done for easy clustering process
                        Collections.sort(allEdges);
                    } else {
                        printString("Edge is already present in the graph.");
                    }
                } else {
                    printString("single vertex forming an edge of graph is not in scope");
                }
            } else {
                //in case invalid input error message is printed to user
                printString("Input is not valid. Vertex should not be null or empty and weight should be more than ZERO.");
            }
        }catch (Exception e){
            printString("Program faced an unexpected exception.");
        }
        return isAdded;
    }

    /*
    sameClusterCheck method
    gets edge as input
    returns boolean value as output
    method is used to check whether the vertices are already in the same cluster
    */
    private boolean sameClusterCheck(Edge edge){
        boolean sameClust = false;

        if(clustNoVertMap != null && !clustNoVertMap.isEmpty()) {
            //loop is created with cluster number and its vertices map
            for (int clustNo : clustNoVertMap.keySet()) {
                Set<String> vertices = clustNoVertMap.get(clustNo);

                //if the same cluster contains both the vertices we can ignore this edge
                if (vertices.contains(edge.getVertex1()) && vertices.contains(edge.getVertex2())) {
                    sameClust = true;
                }
            }
        }
        return sameClust;
    }

    /*
    clusterVertices method
    gets the user tolerance as input
    returns the list of all clusters for that tolerance
     */
    public Set<Set<String>> clusterVertices (float tolerance ){

        int clustNoSeq = ZERO;
        Set<Set<String>> returnSet = null;
        try {
            if(allEdges != null && !allEdges.isEmpty()) {

                clustNoVertMap = new HashMap<>();
                clustNoMaxWeiMap = new HashMap<>();
                vertMaxWeiMap = new HashMap<>();
                vertClustNoMap = new HashMap<>();

                for (Edge edge : allEdges) {
                    int maxClustNo = ZERO;
                    boolean clustRemov = false;

                    //vertices validated whether they belong to same cluster
                    if (!sameClusterCheck(edge)) {
                        clustNoSeq = clustNoSeq + ONE;

                        //edge tolerance calculated
                        float edgeTolerance = getEdgeTolerance(edge);

                        //edge tolerance compared with user tolerance
                        if (edgeTolerance > tolerance) {
                            //edge tolerance more than user tolerance - NO MERGE

                            //if vertex cluster number map contains the vertex1 then
                            //lowest cluster number set to the vertex
                            if (vertClustNoMap.containsKey(edge.getVertex1())) {
                                if (clustNoSeq < vertClustNoMap.get(edge.getVertex1())) {
                                    vertClustNoMap.put(edge.getVertex1(), clustNoSeq);
                                }
                            } else {
                                //if vertex cluster number map does not have the vertex1 then
                                //cluster sequence number set to the vertex and put into the map
                                vertClustNoMap.put(edge.getVertex1(), clustNoSeq);
                            }

                            //no merge case - different cluster number for each vertex
                            clustNoSeq = clustNoSeq + ONE;

                            //if vertex cluster number map contains the vertex2 then
                            //lowest cluster number set to the vertex
                            if (vertClustNoMap.containsKey(edge.getVertex2())) {
                                if (clustNoSeq < vertClustNoMap.get(edge.getVertex2())) {
                                    vertClustNoMap.put(edge.getVertex2(), clustNoSeq);
                                }
                            } else {
                                //if vertex cluster number map does not have the vertex2 then
                                //cluster sequence number set to the vertex and put into the map
                                vertClustNoMap.put(edge.getVertex2(), clustNoSeq);
                            }
                        }
                        //merging the vertices into a cluster
                        else {

                            int minClustNo1 = ONE;
                            int minClustNo2 = ONE;
                            int max1 = ONE;
                            int max2 = ONE;

                            //vertex cluster number map contains both vertex1 and vertex2
                            if (vertClustNoMap.containsKey(edge.getVertex1()) &&
                                    vertClustNoMap.containsKey(edge.getVertex2())) {

                                //find the lowest cluster number and store in minClustNo2
                                minClustNo1 = Math.min(vertClustNoMap.get(edge.getVertex1()), vertClustNoMap.get(edge.getVertex2()));
                                minClustNo2 = Math.min(minClustNo1, clustNoSeq);

                                //find the largest cluster number and store in maxClustNo
                                maxClustNo = Math.max(vertClustNoMap.get(edge.getVertex1()), vertClustNoMap.get(edge.getVertex2()));
                            }
                            //vertex cluster number map does not contain vertex1
                            //but contains vertex 2
                            else if (!vertClustNoMap.containsKey(edge.getVertex1()) &&
                                    vertClustNoMap.containsKey(edge.getVertex2())) {

                                //find the lowest cluster number and store in minClustNo2
                                minClustNo2 = Math.min(clustNoSeq, vertClustNoMap.get(edge.getVertex2()));

                            }
                            //vertex cluster number map does not contain vertex2
                            //but contains vertex 1
                            else if (vertClustNoMap.containsKey(edge.getVertex1()) &&
                                    !vertClustNoMap.containsKey(edge.getVertex2())) {

                                //find the lowest cluster number and store in minClustNo2
                                minClustNo2 = Math.min(clustNoSeq, vertClustNoMap.get(edge.getVertex1()));

                            }
                            //vertex cluster number map do not have both vertices
                            else if (!vertClustNoMap.containsKey(edge.getVertex1()) &&
                                    !vertClustNoMap.containsKey(edge.getVertex2())) {

                                //set cluster number sequence in minClustNo2
                                minClustNo2 = clustNoSeq;

                            }

                            //set the lowest cluster number in the map against the vertices
                            vertClustNoMap.put(edge.getVertex1(), minClustNo2);
                            vertClustNoMap.put(edge.getVertex2(), minClustNo2);

                            //vertex maximum weight map contains both vertex1 and vertex2
                            if (vertMaxWeiMap.containsKey(edge.getVertex1())
                                    && vertMaxWeiMap.containsKey(edge.getVertex2())) {

                                //finding the maximum weight from the map and current edge
                                max1 = Math.max(vertMaxWeiMap.get(edge.getVertex1()), vertMaxWeiMap.get(edge.getVertex2()));
                                max2 = Math.max(max1, edge.getWeight());
                            }
                            //vertex maximum weight map contains only vertex2 and not vertex 1
                            else if (!vertMaxWeiMap.containsKey(edge.getVertex1())
                                    && vertMaxWeiMap.containsKey(edge.getVertex2())) {

                                max2 = Math.max(edge.getWeight(), vertMaxWeiMap.get(edge.getVertex2()));
                            }
                            //vertex maximum weight map contains only vertex1 and not vertex 2
                            else if (vertMaxWeiMap.containsKey(edge.getVertex1())
                                    && !vertMaxWeiMap.containsKey(edge.getVertex2())) {

                                max2 = Math.max(edge.getWeight(), vertMaxWeiMap.get(edge.getVertex1()));
                            }
                            //vertex maximum weight map does not contain both vertex1 and vertex 2
                            else if (!vertMaxWeiMap.containsKey(edge.getVertex1())
                                    && !vertMaxWeiMap.containsKey(edge.getVertex2())) {

                                max2 = edge.getWeight();
                            }
                            //set the maximum weight to the vertices
                            vertMaxWeiMap.put(edge.getVertex1(), max2);
                            vertMaxWeiMap.put(edge.getVertex2(), max2);

                            //vertex belong to same cluster are updated with the cluster number and maximum weight
                            if (maxClustNo != ZERO) {
                                for (String vert : vertClustNoMap.keySet()) {
                                    if (maxClustNo == vertClustNoMap.get(vert)) {
                                        if (minClustNo2 < vertClustNoMap.get(vert)) {
                                            vertClustNoMap.put(vert, minClustNo2);
                                        }
                                        if (max2 > vertMaxWeiMap.get(vert)) {
                                            vertMaxWeiMap.put(vert, max2);
                                        }
                                        clustRemov = true;
                                    }
                                }
                            }

                        }
                        formClusterNoVertMap();
                        formClusterNoWeiMap();
                        if (clustRemov) {
                            clustNoVertMap.remove(maxClustNo);
                            clustNoMaxWeiMap.remove(maxClustNo);
                        }
                    }
                }
            } else{
                printString("Graph is empty.");
            }
            returnSet = getReturnValue();
        }catch (Exception e){
            printString("Program faced an unexpected exception.");
        }
        return returnSet;
    }

    /*
    getReturnValue method
    returns the list of all clusters
     */
    private Set<Set<String>> getReturnValue(){
        Set<Set<String>> allCluesters = null;
        if(clustNoVertMap != null && !clustNoVertMap.isEmpty()){

            //add each cluster in sorted order created a comparator
            allCluesters = new TreeSet<>(new Comparator<Set<String>>() {
                @Override
                public int compare(Set<String> set1, Set<String> set2) {
                    for(String s1 : set1){
                        for(String s2 : set2){
                            //comparing first element of the set to sort
                            //same vertex will not be in another cluster
                            //so first element comparison is enough
                            return s1.compareTo(s2);
                        }
                    }
                    return ZERO;
                }
            });

            //cluster number vertices map is looped as it contains
            // the set of vertices and its cluster number
            for(int clustNo : clustNoVertMap.keySet()){
                Set<String> vertices = clustNoVertMap.get(clustNo);
                allCluesters.add(vertices);
            }
        }
        clearAllMaps();
        return allCluesters;
    }

    /*
    getEdgeTolerance method
    gets the edge as input
    returns the edge tolerance as output
     */
    private float getEdgeTolerance(Edge edge){
        float returnValue = ZERO;
        float denominator = ONE;

        //set default weight as 1
        float weiVert1 = ONE;
        float weiVert2 = ONE;

        //check vertex maximum weight map contains vertex1
        if(vertMaxWeiMap.containsKey(edge.getVertex1())){
            weiVert1 = vertMaxWeiMap.get(edge.getVertex1());
        }

        //check vertex maximum weight map contains vertex2
        if(vertMaxWeiMap.containsKey(edge.getVertex2())){
            weiVert2 = vertMaxWeiMap.get(edge.getVertex2());
        }

        denominator = Math.min(weiVert1, weiVert2);
        returnValue = edge.getWeight()/denominator;
        return returnValue;
    }

    /*
    formClusterNoVertMap method
    forms the cluster number vertices map
     */
    private void formClusterNoVertMap(){

        if(vertClustNoMap != null && !vertClustNoMap.isEmpty()) {
            //vertex cluster number map is looped
            for (String vert : vertClustNoMap.keySet()) {
                int clustNo = vertClustNoMap.get(vert);

                //check cluster number is already in cluster number vertices map
                //get the list and add the current vertex
                if (clustNoVertMap.containsKey(clustNo)) {
                    Set<String> vertices = clustNoVertMap.get(clustNo);
                    vertices.add(vert);
                    clustNoVertMap.put(clustNo, vertices);
                } else {
                    //add the cluster number and vertex into the map
                    Set<String> vertices = new TreeSet<>();
                    vertices.add(vert);
                    clustNoVertMap.put(clustNo, vertices);
                }
            }
        }
        vertMapLowClustNo();
    }

    /*
    vertMapLowClustNo method
    updates cluster number of all vertices in vertex cluster number map
    from cluster number vertices map
    */
    private void vertMapLowClustNo(){

        if(clustNoVertMap != null && !clustNoVertMap.isEmpty()) {
            //loop the cluster number vertices map
            for (int clust : clustNoVertMap.keySet()) {
                Set<String> vertices = clustNoVertMap.get(clust);
                for (String vert : vertClustNoMap.keySet()) {
                    if (vertices.contains(vert)) {
                        //set the lowest cluster number
                        if (clust < vertClustNoMap.get(vert)) {
                            vertClustNoMap.put(vert, clust);
                        }
                    }
                }
            }
        }
    }

    /*
    formClusterNoWeiMap method
    creates the cluster number maximum weight map
     */
    private void formClusterNoWeiMap(){
        if(vertMaxWeiMap != null && !vertMaxWeiMap.isEmpty()) {
            //loops the vertex maximum weight map
            for (String vert : vertMaxWeiMap.keySet()) {
                int weight = vertMaxWeiMap.get(vert);
                for (Integer clustNo : clustNoVertMap.keySet()) {
                    Set<String> vertices = clustNoVertMap.get(clustNo);

                    //check vertex belongs to this cluster
                    if (vertices.contains(vert)) {

                        //cluster is in cluster maximum weight map
                        if (clustNoMaxWeiMap.containsKey(clustNo)) {

                            //set the highest weight
                            if (weight > clustNoMaxWeiMap.get(clustNo)) {
                                clustNoMaxWeiMap.put(clustNo, weight);
                            } else {
                                vertMaxWeiMap.put(vert, clustNoMaxWeiMap.get(clustNo));
                            }
                        } else {
                            clustNoMaxWeiMap.put(clustNo, weight);
                        }
                    }
                }
            }
        }
        vertMapMaxWeight();
    }

    /*
    vertMapMaxWeight map
    updates the maximum weight of all vertices
     */
    private void vertMapMaxWeight(){
        if(clustNoMaxWeiMap != null && !clustNoMaxWeiMap.isEmpty()) {
            //loop the cluster maximum weight map
            for (int clust : clustNoMaxWeiMap.keySet()) {
                Set<String> vertices = clustNoVertMap.get(clust);
                for (String vert : vertMaxWeiMap.keySet()) {
                    int weight = vertMaxWeiMap.get(vert);

                    //cluster contains the vertex
                    if (vertices.contains(vert)) {

                        //set the highest weight
                        if (weight < clustNoMaxWeiMap.get(clust)) {
                            vertMaxWeiMap.put(vert, clustNoMaxWeiMap.get(clust));
                        }
                    }
                }
            }
        }
    }

    /*
    clearAllMaps method
    method clears all the map details so that the next time cluster vertices method is called
    all manipulations happen from the scratch
     */
    private void clearAllMaps(){
        if(clustNoVertMap != null && !clustNoVertMap.isEmpty()){
            clustNoVertMap.clear();
        }
        if(clustNoMaxWeiMap != null && !clustNoMaxWeiMap.isEmpty()){
            clustNoMaxWeiMap.clear();
        }
        if(vertClustNoMap != null && !vertClustNoMap.isEmpty()){
            vertClustNoMap.clear();
        }
        if(vertMaxWeiMap != null && !vertMaxWeiMap.isEmpty()){
            vertMaxWeiMap.clear();
        }
    }

}
