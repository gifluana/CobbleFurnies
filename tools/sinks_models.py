import os
import json

# Define the templates for the files
def get_item_model(color):
    return {
        "parent": "cobblefurnies:block/template/sink_full",
        "textures": {
            "1": f"cobblefurnies:block/sink/{color}"
        }
    }

def get_block_model_bottom(color):
    return {
        "parent": "cobblefurnies:block/template/sink_bottom",
        "textures": {
            "1": f"cobblefurnies:block/sink/{color}"
        }
    }

def get_block_model_bottom_left(color):
    return {
        "parent": "cobblefurnies:block/template/sink_bottom_left",
        "textures": {
            "1": f"cobblefurnies:block/sink/{color}"
        }
    }

def get_block_model_bottom_right(color):
    return {
        "parent": "cobblefurnies:block/template/sink_bottom_right",
        "textures": {
            "1": f"cobblefurnies:block/sink/{color}"
        }
    }

def get_block_model_bottom_middle(color):
    return {
        "parent": "cobblefurnies:block/template/sink_bottom_middle",
        "textures": {
            "1": f"cobblefurnies:block/sink/{color}"
        }
    }

def get_block_model_top(color):
    return {
        "parent": "cobblefurnies:block/template/sink_bottom_top",
        "textures": {
            "1": f"cobblefurnies:block/sink/{color}"
        }
    }

def get_blockstate(color):
    return {
        "multipart": [
            {
                "when": {
                    "facing": "north"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_top"
                }
            },
            {
                "when": {
                    "facing": "east"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_top",
                    "y": 90
                }
            },
            {
                "when": {
                    "facing": "south"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_top",
                    "y": 180
                }
            },
            {
                "when": {
                    "facing": "west"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_top",
                    "y": 270
                }
            },
            {
                "when": {
                    "facing": "north",
                    "connected_left": "true",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_middle"
                }
            },
            {
                "when": {
                    "facing": "east",
                    "connected_left": "true",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_middle",
                    "y": 90
                }
            },
            {
                "when": {
                    "facing": "south",
                    "connected_left": "true",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_middle",
                    "y": 180
                }
            },
            {
                "when": {
                    "facing": "west",
                    "connected_left": "true",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_middle",
                    "y": 270
                }
            },
            {
                "when": {
                    "facing": "north",
                    "connected_left": "true",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_left"
                }
            },
            {
                "when": {
                    "facing": "east",
                    "connected_left": "true",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_left",
                    "y": 90
                }
            },
            {
                "when": {
                    "facing": "south",
                    "connected_left": "true",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_left",
                    "y": 180
                }
            },
            {
                "when": {
                    "facing": "west",
                    "connected_left": "true",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_left",
                    "y": 270
                }
            },
            {
                "when": {
                    "facing": "north",
                    "connected_left": "false",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_right"
                }
            },
            {
                "when": {
                    "facing": "east",
                    "connected_left": "false",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_right",
                    "y": 90
                }
            },
            {
                "when": {
                    "facing": "south",
                    "connected_left": "false",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_right",
                    "y": 180
                }
            },
            {
                "when": {
                    "facing": "west",
                    "connected_left": "false",
                    "connected_right": "true"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom_right",
                    "y": 270
                }
            },
            {
                "when": {
                    "facing": "north",
                    "connected_left": "false",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom"
                }
            },
            {
                "when": {
                    "facing": "east",
                    "connected_left": "false",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom",
                    "y": 90
                }
            },
            {
                "when": {
                    "facing": "south",
                    "connected_left": "false",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom",
                    "y": 180
                }
            },
            {
                "when": {
                    "facing": "west",
                    "connected_left": "false",
                    "connected_right": "false"
                },
                "apply": {
                    "model": f"cobblefurnies:block/sink/{color}_bottom",
                    "y": 270
                }
            }
        ]
    }

def get_lang_entry(color):
    return {f"block.cobblefurnies.{color}_sink": ""}

# Function to save JSON data to a file
def save_json_file(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

# Main function
def generate_files():
    # Read colors from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")

    # Generate files for each color
    lang_entries = {}
    for color in colors:
        # Item model
        item_model_path = os.path.join(base_dir, f"models/item/{color}_sink.json")
        save_json_file(item_model_path, get_item_model(color))

        # Block models
        save_json_file(os.path.join(base_dir, f"models/block/sink/{color}_bottom.json"), get_block_model_bottom(color))
        save_json_file(os.path.join(base_dir, f"models/block/sink/{color}_bottom_left.json"), get_block_model_bottom_left(color))
        save_json_file(os.path.join(base_dir, f"models/block/sink/{color}_bottom_right.json"), get_block_model_bottom_right(color))
        save_json_file(os.path.join(base_dir, f"models/block/sink/{color}_bottom_middle.json"), get_block_model_bottom_middle(color))
        save_json_file(os.path.join(base_dir, f"models/block/sink/{color}_top.json"), get_block_model_top(color))

        # Blockstate
        blockstate_path = os.path.join(base_dir, f"blockstates/{color}_sink.json")
        save_json_file(blockstate_path, get_blockstate(color))

        # Lang entry
        lang_entries.update(get_lang_entry(color))

    # Save the lang file
    lang_path = os.path.join(base_dir, "lang/en_us.json")
    save_json_file(lang_path, lang_entries)

if __name__ == "__main__":
    generate_files()
