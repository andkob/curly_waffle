import pygame
import math

# Constants
SCREEN_WIDTH = 1000
SCREEN_HEIGHT = 800
BOX_WIDTH = 800
BOX_HEIGHT = 600
BOX_LEFT = (SCREEN_WIDTH - BOX_WIDTH) // 2
BOX_TOP = (SCREEN_HEIGHT - BOX_HEIGHT) // 2
NUM_BALLS = 3000
P_RADIUS = 4
GRAVITY = 0.7
DAMPING_FACTOR = 0.8
MIN_VELOCITY = 0.1  # Min velocity threshold to stop bouncing

# Smoothing kernel parameters
smoothingRadius = 150  # Adjusted smoothing radius for better influence

# Ball class
class Particle:
    def __init__(self, position):
        self.pressure = 0.0
        self.density = 0.0
        self.radius = P_RADIUS
        self.color = (50, 150, 255)
        self.position = position
        self.velocity = pygame.math.Vector2(0, 0)
        self.acceleration = pygame.math.Vector2(0, 0)


    def apply_force(self, force):
        self.acceleration += force

    def update(self):
        self.velocity += self.acceleration
        self.position += self.velocity
        self.acceleration *= 0  # Reset acceleration

    def move(self):
        self.update()

        # Collision detection with box walls
        if self.position.x - self.radius <= BOX_LEFT:
            self.position.x = BOX_LEFT + self.radius
            self.velocity.x *= -DAMPING_FACTOR
        elif self.position.x + self.radius >= BOX_LEFT + BOX_WIDTH:
            self.position.x = BOX_LEFT + BOX_WIDTH - self.radius
            self.velocity.x *= -DAMPING_FACTOR

        # Collision detection with ground and ceiling
        if self.position.y - self.radius <= BOX_TOP:
            self.position.y = BOX_TOP + self.radius
            self.velocity.y *= -DAMPING_FACTOR
        elif self.position.y + self.radius >= BOX_TOP + BOX_HEIGHT:
            self.position.y = BOX_TOP + BOX_HEIGHT - self.radius
            self.velocity.y *= -DAMPING_FACTOR

    def draw(self, screen):
        pygame.draw.circle(screen, self.color, (int(self.position.x), int(self.position.y)), self.radius)

def smoothingKernel(radius, dist):
    # first attempt (could not get to work properly though in theory it should)
    #volume = math.pi * radius ** 2
    #value = max(0, radius * radius - dist * dist)
    #return (value * value) / volume
    
    # Gaussian distribution attempt (too sensitive)
    #sigma = radius / 2  # Adjust sigma to control the spread of the Gaussian
    #return math.exp(-0.5 * (dist / sigma) ** 2) / (sigma * math.sqrt(2 * math.pi))
    
    # Good sensitivity with radius 150?? but lags WAYYY too much
    if dist >= radius:
        return 0.0
    else:
        return math.exp(-(dist / radius) ** 2)

def calculateDensity(samplePoint):
    density = 0
    mass = 1

    for particle in particles:
        dist = (particle.position - samplePoint).magnitude()  # Calculate distance to the particle
        influence = smoothingKernel(smoothingRadius, dist)
        #debugDensity(dist, influence) # Debug print
        density += mass * influence

    return density

# Helper debugging function
def debugDensity(dist, influence):
    print("Distance:", dist)  # Debug print
    print("Influence:", influence)  # Debug print

def apply_gravity(particle):
    particle.apply_force(pygame.math.Vector2(0, GRAVITY))

# Function to initialize particles
def initialize_grid():
    NUM_ROWS = int(math.sqrt(NUM_BALLS))
    NUM_COLS = (NUM_BALLS - 1) // NUM_ROWS + 1
    particles.clear()
    
    # Calculate spacing between balls
    spacing_x = (BOX_WIDTH - P_RADIUS * 2) / (NUM_COLS - 1)
    spacing_y = (BOX_HEIGHT - P_RADIUS * 2) / (NUM_ROWS - 1)
    
    # Calculate starting position
    start_x = BOX_LEFT + P_RADIUS
    start_y = BOX_TOP + P_RADIUS
    
    for i in range(NUM_ROWS):
        for j in range(NUM_COLS):
            x = start_x + j * spacing_x
            y = start_y + i * spacing_y
            particles.append(Particle(pygame.math.Vector2(x, y)))
            positions.append(pygame.math.Vector2(x, y))
            

# Initialize pygame
pygame.init()
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("Fluid Simulation")
clock = pygame.time.Clock()

positions = []  # List to store positions of particles
particles = []
initialize_grid()

# Main loop
running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False

    for p in particles:
        apply_gravity(p)

    for p in particles:
        p.move()
        #calculateDensity(p.position)

        
    # Draw everything
    screen.fill((0, 0, 0))
    pygame.draw.rect(screen, (255, 255, 255), (BOX_LEFT, BOX_TOP, BOX_WIDTH, BOX_HEIGHT), 2)  # Draw the box
    for p in particles:
        p.draw(screen)
    pygame.display.flip()

    clock.tick(60)  # Cap the frame rate

pygame.quit()
