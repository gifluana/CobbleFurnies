import os
import json

def get_loot_table(material, block_name):
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [
                    {
                        "type": "minecraft:item",
                        "name": f"cobblefurnies:{material}_{block_name}"
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

def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def generate_loot_tables():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    # Ask user for palette type
    palette_type = input("Which palette you want? (Colored/Wood): ").strip().lower()
    
    if palette_type == "colored":
        palette_file = "colors.json"
    elif palette_type == "wood":
        palette_file = "woods.json"
    else:
        print("Invalid selection. Exiting.")
        return
    
    palette_path = os.path.join(script_dir, palette_file)
    
    try:
        with open(palette_path, "r", encoding="utf-8") as f:
            materials = json.load(f)
    except FileNotFoundError:
        print(f"Error: {palette_file} not found.")
        return
    
    # Ask user for block types (comma-separated)
    block_names = input("Which block types do you want? (comma-separated, e.g., cabinet, table, chair, drawer): ").strip().lower().split(", ")
    
    base_dir = os.path.join(script_dir, "cobblefurnies", "loot_tables", "blocks")
    
    for material in materials:
        for block_name in block_names:
            file_path = os.path.join(base_dir, f"{material}_{block_name}.json")
            loot_table = get_loot_table(material, block_name)
            save_json_file(file_path, loot_table)
            print(f"Generated: {file_path}")

if __name__ == "__main__":
    generate_loot_tables()