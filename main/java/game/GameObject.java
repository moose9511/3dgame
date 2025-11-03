package game;
import static org.lwjgl.opengl.GL11.*;

class GameObject {
 	public float x, y, z;
 	public float rotX, rotY, rotZ;
 	public float scaleX = 1, scaleY = 1, scaleZ = 1;
 	public float r = 1f, g = 1f, b = 1f;
 	
 	Mesh mesh;
 	AABB hitboxRect;
 	BoundingSphere hitboxSphere;
    
    public GameObject(float x, float y, float z, String modelPath) {
        this.x = x;
        this.y = y;
        this.z = z;
        mesh = mesh.loadOBJ(modelPath);
    }
    
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setRotation(float rotX, float rotY, float rotZ) {
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }
    
    public void setScale(float scale) {
        this.scaleX = this.scaleY = this.scaleZ = scale;
    }
    
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }
    
    public void setHitbox(AABB aabb) {
    	hitboxRect = aabb;
    	hitboxSphere = null;
    }
    public void setHitbox(BoundingSphere boundSphere) {
    	hitboxSphere = boundSphere;
    	hitboxRect = null;
    }
    
    public void setColour(float r, float g, float b) {
    	this.r = r;
    	this.g = g;
    	this.b = b;
    }
    protected void applyTransform() {
        glPushMatrix();
        glTranslatef(x, y, z);
        glRotatef(rotX, 1, 0, 0);
        glRotatef(rotY, 0, 1, 0);
        glRotatef(rotZ, 0, 0, 1);
        glScalef(scaleX, scaleY, scaleZ);
    }
    

    public void render() {
    	glColor3f(r, g, b);
    	 glBegin(GL_TRIANGLES);
         for (int i = 0; i < mesh.vertices.size(); i++) {
             if (i < mesh.normals.size()) {
                 float[] n = mesh.normals.get(i);
                 glNormal3f(n[0], n[1], n[2]);
             }
             if (i < mesh.texCoords.size()) {
                 float[] t = mesh.texCoords.get(i);
                 glTexCoord2f(t[0], t[1]);
             }
             float[] v = mesh.vertices.get(i);
             glVertex3f(v[0], v[1], v[2]);
         }
         
         glEnd();
    }
}
