package game;

class BoundingSphere {
    public float x, y, z;     // Center position
    public float radius;
    
    public BoundingSphere(float x, float y, float z, float radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }
    
    // Update position (call when object moves)
    public void updatePosition(float x, float y, float z) {
        this.x = x;
        this.y = y; 
        this.z = z;
    }
    
    // Check collision with another sphere
    public boolean intersects(BoundingSphere other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        float distanceSquared = dx * dx + dy * dy + dz * dz;
        float radiusSum = this.radius + other.radius;
        
        return distanceSquared <= (radiusSum * radiusSum);
    }
    
    // Get distance to another sphere
    public float distanceTo(BoundingSphere other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
