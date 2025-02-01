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
                "item": "minecraft:spruce_planks"
            }
        },
        "pattern": [
            "# ",
            "##",
            "//"
        ],
        "result": {
            "count": 6,
            "id": f"cobblefurnies:{color}_chair"
        }
    }

def get_sofa_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "sofa",
        "key": {
            "#": {
                "item": f"minecraft:{color}_wool"
            },
            "/": {
                "item": "minecraft:stick"
            }
        },
        "pattern": [
            "#  ",
            "###",
            "/ /"
        ],
        "result": {
            "count": 3,
            "id": f"cobblefurnies:{color}_sofa"
        }
    }

def get_stool_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "stools",
        "key": {
            "#": {
                "item": f"minecraft:{color}_wool"
            },
            "/": {
                "item": "minecraft:spruce_planks"
            }
        },
        "pattern": [
            "##",
            "//"
        ],
        "result": {
            "count": 6,
            "id": f"cobblefurnies:{color}_stool"
        }
    }

def get_table_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "tables",
        "key": {
            "#": {
                "item": f"minecraft:{color}_wool"
            },
            "/": {
                "item": "minecraft:stick"
            },
            "P": {
                "item": "minecraft:spruce_planks"
            }
        },
        "pattern": [
            "   ",
            "P#P",
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
        "group": "sinks",
        "key": {
            "C": {
                "item": f"minecraft:{color}_concrete"
            },
            "#": {
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
        "group": "stoves",
        "key": {
            "C": {
                "item": f"minecraft:{color}_concrete"
            },
            "#": {
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

def get_drawer_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "drawers",
        "key": {
            "C": {
                "item": f"minecraft:{color}_concrete"
            },
            "#": {
                "item": "minecraft:calcite"
            },
            "B": {
                "item": "minecraft:barrel"
            }
        },
        "pattern": [
            " B ",
            "###",
            "CCC"
        ],
        "result": {
            "count": 8,
            "id": f"cobblefurnies:{color}_drawer"
        }
    }

def get_cabinetry_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "cabinetry",
        "key": {
            "C": {
                "item": f"minecraft:{color}_concrete"
            },
            "#": {
                "item": "minecraft:calcite"
            }
        },
        "pattern": [
            "###",
            "CCC"
        ],
        "result": {
            "count": 8,
            "id": f"cobblefurnies:{color}_cabinetry"
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
        # chair_path = os.path.join(base_dir, f"{color}_chair.json")
        # save_json_file(chair_path, get_chair_recipe(color))

        # Stool recipe
        # stool_path = os.path.join(base_dir, f"{color}_stool.json")
        # save_json_file(stool_path, get_stool_recipe(color))

        # Sofa recipe
        # sofa_path = os.path.join(base_dir, f"{color}_sofa.json")
        # save_json_file(sofa_path, get_sofa_recipe(color))

        # Drawer recipe
        # drawer_path = os.path.join(base_dir, f"{color}_drawer.json")
        # save_json_file(drawer_path, get_drawer_recipe(color))

        # Table recipe
        table_path = os.path.join(base_dir, f"{color}_table.json")
        save_json_file(table_path, get_table_recipe(color))

        # Sink recipe
        # sink_path = os.path.join(base_dir, f"{color}_sink.json")
        # save_json_file(sink_path, get_sink_recipe(color))

        # Stove recipe
        # stove_path = os.path.join(base_dir, f"{color}_stove.json")
        # save_json_file(stove_path, get_stove_recipe(color))

        # Cabinetry recipe
        # cabinetry_path = os.path.join(base_dir, f"{color}_cabinetry.json")
        # save_json_file(cabinetry_path, get_cabinetry_recipe(color))

if __name__ == "__main__":
    generate_recipes()