import os
import json

# Define the templates for recipes
def get_chair_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "chairs",
        "key": {
            "#": {
                "item": f"minecraft:{color}_wool"
            },
            "/": {
                "item": "minecraft:stick"
            }
        },
        "pattern": [
            "# ",
            "##",
            "//"
        ],
        "result": {
            "count": 3,
            "id": f"cobblefurnies:{color}_chair"
        }
    }

def get_table_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "chairs",
        "key": {
            "#": {
                "item": f"minecraft:{color}_wool"
            },
            "/": {
                "item": "minecraft:stick"
            }
        },
        "pattern": [
            "   ",
            "###",
            "/ /"
        ],
        "result": {
            "count": 3,
            "id": f"cobblefurnies:{color}_table"
        }
    }

def get_sink_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "chairs",
        "key": {
            "#": {
                "item": f"minecraft:{color}_concrete"
            },
            "C": {
                "item": "minecraft:calcite"
            },
            "B": {
                "item": "minecraft:bucket"
            }
        },
        "pattern": [
            " B ",
            "###",
            "CCC"
        ],
        "result": {
            "count": 8,
            "id": f"cobblefurnies:{color}_sink"
        }
    }

def get_stove_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "chairs",
        "key": {
            "#": {
                "item": f"minecraft:{color}_concrete"
            },
            "C": {
                "item": "minecraft:calcite"
            },
            "B": {
                "item": "minecraft:flint_and_steel"
            }
        },
        "pattern": [
            " B ",
            "###",
            "CCC"
        ],
        "result": {
            "count": 8,
            "id": f"cobblefurnies:{color}_stove"
        }
    }

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_recipes():
    # Read colors from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for recipes
    base_dir = os.path.join(script_dir, "cobblefurnies", "recipes")

    # Generate recipes for each color and block
    for color in colors:
        # Chair recipe
        chair_path = os.path.join(base_dir, f"{color}_chair.json")
        save_json_file(chair_path, get_chair_recipe(color))

        # Table recipe
        table_path = os.path.join(base_dir, f"{color}_table.json")
        save_json_file(table_path, get_table_recipe(color))

        # Sink recipe
        sink_path = os.path.join(base_dir, f"{color}_sink.json")
        save_json_file(sink_path, get_sink_recipe(color))

        # Stove recipe
        stove_path = os.path.join(base_dir, f"{color}_stove.json")
        save_json_file(stove_path, get_stove_recipe(color))

if __name__ == "__main__":
    generate_recipes()