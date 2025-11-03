package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Camera {
	public float posx, posy, posz;
	private float rotx, roty, rotz;
	private float speed = 10f;
	public float sensitivity = .24f;
	
	public float vertSpeed = 0f;
	public final float gravity = -9.8f;
	
	public AABB hitboxRect;
	public BoundingSphere hitboxSphere;
	
	boolean checkIntersect = false;
	
	public Camera(float x, float y, float z) {
		posx = x; posy = y; posz = z;
		rotx = 0; roty = 0; rotz = 0;
	}
	
	public void applyTransform() {
		glRotatef(rotx, 1, 0, 0);
		glRotatef(roty, 0, 1, 0);
		glRotatef(rotz, 0, 0, 1);
		
		glTranslatef(-posx, -posy, -posz);
	}
	
	public void setHitbox(AABB aabb) {
    	hitboxRect = aabb;
    	hitboxSphere = null;
    }
    public void setHitbox(BoundingSphere boundSphere) {
    	hitboxSphere = boundSphere;
    	hitboxRect = null;
    }
	
	public void moveForward(float distance) {
		posx += distance * (float) Math.sin(Math.toRadians(roty));
		posz -= distance * (float) Math.cos(Math.toRadians(roty));
	}
	
	public void moveSideways(float distance) {
		posx += distance * (float) Math.sin(Math.toRadians(roty - 90));
        posz -= distance * (float) Math.cos(Math.toRadians(roty - 90));
	}
	
	public void moveUp(float distance) {
		posy += distance;
	}
	
	float lastX = 0, lastY = 0;
	public void addRot(float xRotAdd, float yRotAdd) {
		rotx += (xRotAdd - lastX) * sensitivity;
		roty += (yRotAdd - lastY) * sensitivity;
		
		if (rotx >= 90) rotx = 90;
        if (rotx <= -90) rotx = -90;
        
        if (roty > 360) roty -= 360;
        if (roty < 0) roty += 360;
        
        lastX = xRotAdd;
        lastY = yRotAdd;
	}
	
	
	public boolean intersects(GameObject target) {
		if(hitboxRect != null && target.hitboxRect != null) {
			return hitboxRect.intersects(target.hitboxRect);
		} else if(hitboxSphere != null && target.hitboxSphere != null) {
			return hitboxSphere.intersects(target.hitboxSphere);
		} 
		return false;
	}
	
	public void updateEvents(long window, float deltaTime) {
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            moveForward(speed * deltaTime);
        }
    	if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            moveForward(-speed * deltaTime);
        }
    	if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            moveSideways(-speed * deltaTime);
        }
    	if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
    		moveSideways(speed * deltaTime);
        }
    	if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS && vertSpeed == 0f) {
            vertSpeed = 50f;
            posy += vertSpeed * deltaTime;
        }
    	
    	hitboxRect.updatePosition(posx, posy, posz);
    	
//    	if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
//    		camera.moveUp(-speed * deltaTime);
//        }
    	
	}
}
