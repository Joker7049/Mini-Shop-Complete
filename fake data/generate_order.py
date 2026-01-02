import csv
import random
from faker import Faker
from datetime import datetime

fake = Faker()

# Configuration
NUM_ROWS = 5000  # Let's generate 5000 orders
FILE_NAME = 'orders_data.csv'

# Based on your previous steps:
# Users table has IDs from 1 to 1000 (roughly)
# Products table has IDs from 1 to 10000 (roughly)
USER_ID_RANGE = (1, 1000)
PRODUCT_ID_RANGE = (1, 10000)

statuses = ['DELIVERED', 'PENDING', 'SHIPPED']

print(f"Generating {NUM_ROWS} orders...")

with open(FILE_NAME, 'w', newline='', encoding='utf-8') as f:
    writer = csv.writer(f)
    
    for _ in range(NUM_ROWS):
        count = random.randint(1, 5)
        # Generate a random date in the last 2 years
        order_date = fake.date_time_between(start_date='-2y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
        status = random.choice(statuses)
        total_price = round(random.uniform(20.0, 2000.0), 2)
        product_id = random.randint(*PRODUCT_ID_RANGE)
        user_id = random.randint(*USER_ID_RANGE)
        
        # Order: count, order_date, status, total_price, product_id, user_id
        writer.writerow([count, order_date, status, total_price, product_id, user_id])

print(f"Done! Created {FILE_NAME}")