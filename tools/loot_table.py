import os
import json

# Define the template for loot tables
def get_loot_table(color, block_name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "name": f"cobblefurnies:{color}_{block_name}"
                    }
                ],
                "conditions": [
                    {
                        "condition": "minecraft:survives_explosion"
                    }
                ]
            }
        ]
    }

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_loot_tables():
    # Read colors from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for loot tables
    base_dir = os.path.join(script_dir, "cobblefurnies", "loot_tables", "blocks")

    # Blocks for which loot tables will be generated
    blocks = ["drawer"]

    # Generate loot tables for each color and block
    for color in colors:
        for block in blocks:
            file_path = os.path.join(base_dir, f"{color}_{block}.json")
            loot_table = get_loot_table(color, block)
            save_json_file(file_path, loot_table)

if __name__ == "__main__":
    generate_loot_tables()
