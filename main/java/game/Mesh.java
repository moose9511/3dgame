package game;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	public List<float[]> vertices = new ArrayList<>();
	public List<float[]> normals = new ArrayList<>();
	public List<float[]> texCoords = new ArrayList<>();
	public List<int[]> faces = new ArrayList<>();
    
    public static Mesh loadOBJ(String filepath) {
        Mesh mesh = new Mesh();
        List<float[]> tempVertices = new ArrayList<>();
        List<float[]> tempNormals = new ArrayList<>();
        List<float[]> tempTexCoords = new ArrayList<>();
        
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
        		new java.io.FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split("\\s+");
                
                // Vertex positions
                if (parts[0].equals("v")) {
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    tempVertices.add(new float[]{x, y, z});
                }
                // Vertex normals
                else if (parts[0].equals("vn")) {
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    tempNormals.add(new float[]{x, y, z});
                }
                // Texture coordinates
                else if (parts[0].equals("vt")) {
                    float u = Float.parseFloat(parts[1]);
                    float v = Float.parseFloat(parts[2]);
                    tempTexCoords.add(new float[]{u, v});
                }
                // Faces
                else if (parts[0].equals("f")) {
                    int[] faceIndices = new int[9]; // 3 vertices × 3 indices each
                    for (int i = 0; i < 3; i++) {
                        String[] indices = parts[i + 1].split("/");
                        faceIndices[i * 3] = Integer.parseInt(indices[0]) - 1; // vertex
                        if (indices.length > 1 && !indices[1].isEmpty()) {
                            faceIndices[i * 3 + 1] = Integer.parseInt(indices[1]) - 1; // texture
                        } else {
                            faceIndices[i * 3 + 1] = -1;
                        }
                        if (indices.length > 2) {
                            faceIndices[i * 3 + 2] = Integer.parseInt(indices[2]) - 1; // normal
                        } else {
                            faceIndices[i * 3 + 2] = -1;
                        }
                    }
                    mesh.faces.add(faceIndices);
                }
            }
            
            // Convert indexed data to flat arrays
            for (int[] face : mesh.faces) {
                for (int i = 0; i < 3; i++) {
                    int vIdx = face[i * 3];
                    int tIdx = face[i * 3 + 1];
                    int nIdx = face[i * 3 + 2];
                    
                    mesh.vertices.add(tempVertices.get(vIdx));
                    if (nIdx >= 0 && nIdx < tempNormals.size()) {
                    	mesh.normals.add(tempNormals.get(nIdx));
                    }
                    if (tIdx >= 0 && tIdx < tempTexCoords.size()) {
                    	mesh.texCoords.add(tempTexCoords.get(tIdx));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading model: " + filepath);
            e.printStackTrace();
        }
        
        return mesh;
    }
}
