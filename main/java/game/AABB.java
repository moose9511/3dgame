package game;

class AABB {
    public float minX, minY, minZ;  // Minimum corner
    public float maxX, maxY, maxZ;  // Maximum corner
    
    public AABB(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
    	if(minX > maxX) {
    		this.minX = maxX;
    		this.maxX = minX;
    	} else {
    		this.minX = minX;
            this.maxX = maxX;
    	}
    	if(minY > maxY) {
    		this.minY = maxY;
    		this.maxY = minY;
    	} else {
    		this.minY = minY;
            this.maxY = maxY;
    	}
    	if(minZ > maxZ) {
    		this.minZ = maxZ;
    		this.maxZ = minZ;
    	} else {
    		this.maxY = maxY;
            this.maxZ = maxZ;
    	}
    }
    
    // Create AABB from center and size
    public static AABB fromCenterAndSize(float x, float y, float z, float width, float height, float depth) {
        float halfW = width / 2;
        float halfH = height / 2;
        float halfD = depth / 2;
        return new AABB(x - halfW, y - halfH, z - halfD, x + halfW, y + halfH, z + halfD);
    }
    
    // Update position (maintains size)
    public void updatePosition(float x, float y, float z) {
        float width = maxX - minX;
        float height = maxY - minY;
        float depth = maxZ - minZ;
        
        minX = x - width / 2;
        maxX = x + width / 2;
        minY = y - height / 2;
        maxY = y + height / 2;
        minZ = z - depth / 2;
        maxZ = z + depth / 2;
    }
    
    // Check collision with another AABB
    public boolean intersects(AABB other) {
        return (this.minX <= other.maxX && this.maxX >= other.minX) &&
               (this.minY <= other.maxY && this.maxY >= other.minY) &&
               (this.minZ <= other.maxZ && this.maxZ >= other.minZ);
    }
    
    // Check if point is inside
    public boolean containsPoint(float x, float y, float z) {
        return x >= minX && x <= maxX &&
               y >= minY && y <= maxY &&
               z >= minZ && z <= maxZ;
    }
}
