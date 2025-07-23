import os
import json

# Define the templates for recipes
def get_cabinet_recipe(wood):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 6,
                "item": f"minecraft:{wood}_planks"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{wood}_cabinet",
            "count": 1
        }
    }

def get_poke_wool_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 1,
                "item": f"minecraft:{color}_wool"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_poke_wool",
            "count": 1
        }
    }


def get_wood_chair_recipe(wood):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{wood}_planks"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{wood}_chair",
            "count": 2
        }
    }

def get_wood_table_recipe(wood):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{wood}_planks"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{wood}_table",
            "count": 1
        }
    }

def get_colored_chair_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{color}_wool"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_chair",
            "count": 2
        }
    }

def get_colored_stool_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 2,
                "item": f"minecraft:{color}_wool"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_stool",
            "count": 2
        }
    }

def get_colored_table_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 2,
                "item": "minecraft:oak_planks"
            },
            {
                "count": 1,
                "item": f"minecraft:{color}_wool"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_table",
            "count": 1
        }
    }

def get_sofa_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{color}_wool"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_sofa",
            "count": 1
        }
    }

def get_sink_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{color}_concrete"
            },
            {
                "count": 3,
                "item": "minecraft:calcite"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_sink",
            "count": 1
        }
    }

def get_stove_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{color}_concrete"
            },
            {
                "count": 3,
                "item": "minecraft:calcite"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_stove",
            "count": 1
        }
    }

def get_cabinetry_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{color}_concrete"
            },
            {
                "count": 3,
                "item": "minecraft:calcite"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_cabinetry",
            "count": 1
        }
    }

def get_drawer_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 3,
                "item": f"minecraft:{color}_concrete"
            },
            {
                "count": 3,
                "item": "minecraft:calcite"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_drawer",
            "count": 1
        }
    }

def get_poke_wool_carpet_recipe(color):
    return {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": "carpet",
        "key": {
            "C": {
                "item": f"cobblefurnies:{color}_poke_wool"
            }
        },
        "pattern": [
            "CC"
        ],
        "result": {
            "count": 3,
            "id": f"cobblefurnies:{color}_poke_wool_carpet"
        }
    }

def get_curtain_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 2,
                "item": f"minecraft:{color}_wool"
            },
            {
                "count": 1,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_curtain",
            "count": 2
        }
    }

def get_armchair_recipe(color):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 2,
                "item": f"minecraft:{color}_wool"
            },
            {
                "count": 3,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{color}_armchair",
            "count": 2
        }
    }

def get_wall_support_recipe(wood):
    return {
        "type": "cobblefurnies:furni_crafting",
        "materials": [
            {
                "count": 1,
                "item": f"minecraft:{wood}_planks"
            },
            {
                "count": 2,
                "item": "minecraft:stick"
            }
        ],
        "result": {
            "id": f"cobblefurnies:{wood}_wall_support",
            "count": 4
        }
    }

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_recipes():
    # Read colors and woods from the JSON files
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")
    woods_file = os.path.join(script_dir, "woods.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    with open(woods_file, "r", encoding="utf-8") as f:
        woods = json.load(f)

    # Base directory for recipes
    base_dir = os.path.join(script_dir, "cobblefurnies", "recipes")

    # Generate recipes for each color
    #for color in colors:
    #    armchair_path = os.path.join(base_dir, f"{color}_armchair.json")
    #    save_json_file(armchair_path, get_armchair_recipe(color))

    #    curtain_path = os.path.join(base_dir, f"{color}_curtain.json")
    #    save_json_file(curtain_path, get_curtain_recipe(color))

    #    poke_wool_path = os.path.join(base_dir, f"{color}_poke_wool.json")
    #    save_json_file(poke_wool_path, get_poke_wool_recipe(color))
        
    #    colored_chair_path = os.path.join(base_dir, f"{color}_chair.json")
    #    save_json_file(colored_chair_path, get_colored_chair_recipe(color))

    #    colored_stool_path = os.path.join(base_dir, f"{color}_stool.json")
    #    save_json_file(colored_stool_path, get_colored_stool_recipe(color))

    #    sofa_path = os.path.join(base_dir, f"{color}_sofa.json")
    #    save_json_file(sofa_path, get_sofa_recipe(color))

    #    colored_table_path = os.path.join(base_dir, f"{color}_table.json")
    #    save_json_file(colored_table_path, get_colored_table_recipe(color))
        
    #    sink_path = os.path.join(base_dir, f"{color}_sink.json")
    #   save_json_file(sink_path, get_sink_recipe(color))
        
    #   stove_path = os.path.join(base_dir, f"{color}_stove.json")
    #    save_json_file(stove_path, get_stove_recipe(color))

    #    cabinetry_path = os.path.join(base_dir, f"{color}_cabinetry.json")
    #    save_json_file(cabinetry_path, get_cabinetry_recipe(color))

    #    colored_drawer_path = os.path.join(base_dir, f"{color}_drawer.json")
    #    save_json_file(colored_drawer_path, get_drawer_recipe(color))

    # Generate recipes for each wood type
    for wood in woods:
        wall_support_path = os.path.join(base_dir, f"{wood}_wall_support.json")
        save_json_file(wall_support_path, get_wall_support_recipe(wood))

    #    cabinet_path = os.path.join(base_dir, f"{wood}_cabinet.json")
    #    save_json_file(cabinet_path, get_cabinet_recipe(wood))

    #    wood_chair_path = os.path.join(base_dir, f"{wood}_chair.json")
    #    save_json_file(wood_chair_path, get_wood_chair_recipe(wood))

    #    wood_table_path = os.path.join(base_dir, f"{wood}_table.json")
    #    save_json_file(wood_table_path, get_wood_table_recipe(wood))

if __name__ == "__main__":
    generate_recipes()
