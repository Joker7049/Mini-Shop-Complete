import csv
import random
from faker import Faker

fake = Faker()

# Configuration
NUM_ROWS = 10000
FILE_NAME = 'products_data.csv'

categories = ['Electronics', 'Home & Kitchen', 'Fashion', 'Books', 'Beauty', 'Sports', 'Toys']
discount_tags = ['NEW', '10% OFF', 'SALE', 'LIMITED', None]

print(f"Generating {NUM_ROWS} rows...")

with open(FILE_NAME, 'w', newline='', encoding='utf-8') as f:
    writer = csv.writer(f)
    
    # We do NOT include 'id' because it is AUTO_INCREMENT in your MySQL
    # The order here must match the order we use in the LOAD DATA command later
    for _ in range(NUM_ROWS):
        name = fake.catch_phrase()
        description = fake.paragraph(nb_sentences=3)
        price = round(random.uniform(10.0, 500.0), 2)
        quantity = random.randint(1, 100)
        image_url = f"https://picsum.photos/seed/{random.randint(1,1000)}/200/300"
        category = random.choice(categories)
        discount_tag = random.choice(discount_tags)
        # bit(1) in MySQL is usually loaded as 0 or 1
        is_best_seller = random.choice([0, 1])
        old_price = round(price * random.uniform(1.1, 1.5), 2)
        rating = round(random.uniform(1.0, 5.0), 1)

        writer.writerow([
            description, name, price, quantity, image_url, 
            category, discount_tag, is_best_seller, old_price, rating
        ])

print(f"Done! Created {FILE_NAME}")