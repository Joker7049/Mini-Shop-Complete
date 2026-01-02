import csv
import random
from faker import Faker

fake = Faker()

# Configuration
NUM_ROWS = 1000
FILE_NAME = 'users_data.csv'

print(f"Generating {NUM_ROWS} users...")

# Use a set to ensure usernames are unique
unique_usernames = set()
while len(unique_usernames) < NUM_ROWS:
    # Adding a small random number to the username to guarantee uniqueness
    name = fake.user_name() + str(random.randint(1, 9999))
    unique_usernames.add(name)

usernames_list = list(unique_usernames)

with open(FILE_NAME, 'w', newline='', encoding='utf-8') as f:
    writer = csv.writer(f)
    
    for i in range(NUM_ROWS):
        username = usernames_list[i]
        # Generating a fake hashed-style password
        password = "password123" # In a real app, you'd use a BCrypt hash
        role = random.choice(['ADMIN', 'USER'])
        
        # Order: password, role, username (matches the LOAD DATA command below)
        writer.writerow([password, role, username])

print(f"Done! Created {FILE_NAME}")