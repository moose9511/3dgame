package game;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.lang.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


class Game {
	public static void main(String[] args) {
		Gaming g = new Gaming();
		g.run();
	}
}

class Gaming {
	
	public void main(String[] args) {
		run();
	}
	
	public long window;
	ArrayList<GameObject> objs = new ArrayList<GameObject>();
	Camera camera = new Camera(0, 20f, 0);
	
	float deltaTime = 0f;
	float forward = 0f;
	float sideways = 0f;
	
	public void run() {
		init();
		setupScene();
		double startTime = 0, endTime = 0;
		
		// main game loop
		while(!glfwWindowShouldClose(window)) {
			deltaTime = (float) (endTime - startTime);
			startTime = glfwGetTime();
			
			renderFrame();
			camera.updateEvents(window, deltaTime);
			glfwSwapBuffers(window);
	        glfwPollEvents();
	        
	        endTime = glfwGetTime();
		};
	}
	
	
	public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
        window = glfwCreateWindow(1000, 1000, "GAME", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        setupCallbacks();
        centerWindow();
        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        
        GL.createCapabilities();
        setupOpenGL();
    }
    
	
	
    private void setupCallbacks() {
    	glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {
    		camera.addRot((float)ypos *camera.sensitivity, (float)xpos * camera.sensitivity);
    	});
    	
    	glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });
        
    }
    
    private void centerWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetFramebufferSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2);
        }
    }
    
    private void setupOpenGL() {
        glClearColor(0f, 0.6666f, 0.8705f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        
        // Setup lighting
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        
        FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(new float[]{10.0f, 15.0f, 10.0f, 1f}).flip();
        
        FloatBuffer lightAmbient = BufferUtils.createFloatBuffer(4);
        lightAmbient.put(new float[]{.5f, .5f, .5f, 1f}).flip();
        
        FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4);
        lightDiffuse.put(new float[]{0.3f, 0.3f, 0.3f, 1.0f}).flip();
        
        glLightfv(GL_LIGHT0, GL_POSITION, lightPos);
        glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
    }
	
	public void renderFrame() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Setup projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        // Get window size
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, width, height);
        
        glViewport(0, 0, width.get(0), height.get(0));
        
        float aspect = (float) width.get(0) / (float) height.get(0);
        perspective(30f, aspect, .1f, 1000f);
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(sideways, 0, forward);
        
        camera.applyTransform();
        
        for(GameObject obj : objs)  {
        	
        	if(camera.checkIntersect && camera.intersects(obj)) {
        		camera.vertSpeed = 0f;
        		camera.checkIntersect = false;
        	}
        	glPushMatrix();
        	obj.applyTransform();
        	obj.render();
        	glPopMatrix();
        }
        
        if(camera.checkIntersect) {
        	camera.posy += camera.vertSpeed * deltaTime;
        	camera.vertSpeed += camera.gravity * deltaTime;
        	camera.checkIntersect = false;
        }
        
        camera.checkIntersect = true;
        
        glfwPollEvents();
	}
	
	private void setupScene() {
		camera.setHitbox(AABB.fromCenterAndSize(camera.posx, camera.posy, camera.posz, 3f, 3f, 3f));
		objs.add(new GameObject(100f, 80f, 0f, "circle.obj"));
		objs.getLast().setColour(0.8196f, 0.8705f, 0.4352f);
		objs.getLast().setScale(30f);
		objs.add(new GameObject(0, -.3f, -10, "donut.obj"));
		objs.getLast().setColour(.1176f, .8588f, .001f);
		objs.getLast().setScale(10f, 1f, 10f);
		objs.getLast().setHitbox(new AABB(-100f, 100f, -10f, -.3f, -100f, 100f));
	}
	private void perspective(float fovy, float aspect, float zNear, float zFar) {
        float fH = (float) Math.tan(Math.toRadians(fovy)) * zNear;
        float fW = fH * aspect;
        glFrustum(-fW, fW, -fH, fH, zNear, zFar);
    }
	
}



