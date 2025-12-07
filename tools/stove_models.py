import os
import json

def get_blockstate(color):
    """Generate blockstate JSON for a stove color with proper pot rotation"""

    multipart = []

    # Pot colors available
    pot_colors = ["red", "yellow", "white", "pink", "green", "blue", "black"]

    # Add pot models with rotation for each facing direction
    for pot_color in pot_colors:
        # North facing (no rotation)
        multipart.append({
            "when": {
                "facing": "north",
                "has_pot": "true",
                "pot_color": pot_color
            },
            "apply": {
                "model": f"cobblefurnies:block/stove/pot_{pot_color}"
            }
        })

        # East facing (90° rotation)
        multipart.append({
            "when": {
                "facing": "east",
                "has_pot": "true",
                "pot_color": pot_color
            },
            "apply": {
                "model": f"cobblefurnies:block/stove/pot_{pot_color}",
                "y": 90
            }
        })

        # South facing (180° rotation)
        multipart.append({
            "when": {
                "facing": "south",
                "has_pot": "true",
                "pot_color": pot_color
            },
            "apply": {
                "model": f"cobblefurnies:block/stove/pot_{pot_color}",
                "y": 180
            }
        })

        # West facing (270° rotation)
        multipart.append({
            "when": {
                "facing": "west",
                "has_pot": "true",
                "pot_color": pot_color
            },
            "apply": {
                "model": f"cobblefurnies:block/stove/pot_{pot_color}",
                "y": 270
            }
        })

    # Add top models (facing only)
    for facing, rotation in [("north", None), ("east", 90), ("south", 180), ("west", 270)]:
        apply_dict = {"model": f"cobblefurnies:block/stove/{color}_top"}
        if rotation is not None:
            apply_dict["y"] = rotation

        multipart.append({
            "when": {"facing": facing},
            "apply": apply_dict
        })

    # Add bottom models (facing + connected states)
    bottom_types = [
        ("middle", "true", "true"),
        ("left", "true", "false"),
        ("right", "false", "true"),
        ("", "false", "false")  # No suffix for standalone
    ]

    for bottom_suffix, connected_left, connected_right in bottom_types:
        model_name = f"{color}_bottom_{bottom_suffix}" if bottom_suffix else f"{color}_bottom"

        for facing, rotation in [("north", None), ("east", 90), ("south", 180), ("west", 270)]:
            apply_dict = {"model": f"cobblefurnies:block/stove/{model_name}"}
            if rotation is not None:
                apply_dict["y"] = rotation

            multipart.append({
                "when": {
                    "facing": facing,
                    "connected_left": connected_left,
                    "connected_right": connected_right
                },
                "apply": apply_dict
            })

    return {"multipart": multipart}


def save_json_file(path, data):
    """Save JSON data to a file"""
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)


def generate_blockstates():
    """Generate blockstate files for all colors"""
    # Read colors from the JSON file in the same directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    colors_file = os.path.join(script_dir, "colors.json")

    # Check if colors.json exists
    if not os.path.exists(colors_file):
        print(f"Erro: Arquivo 'colors.json' não encontrado em {script_dir}")
        print("Por favor, crie um arquivo colors.json com a lista de cores.")
        print('Exemplo: ["black", "white", "red", "blue", ...]')
        return

    with open(colors_file, "r", encoding="utf-8") as f:
        colors = json.load(f)

    # Base directory for cobblefurnies
    base_dir = os.path.join(script_dir, "cobblefurnies")

    # Generate blockstate for each color
    for color in colors:
        blockstate_path = os.path.join(base_dir, f"blockstates/{color}_stove.json")
        save_json_file(blockstate_path, get_blockstate(color))
        print(f"✓ Gerado: {color}_stove.json")

    print(f"\n✓ Total de {len(colors)} blockstates gerados com sucesso!")
    print(f"Localização: {os.path.join(base_dir, 'blockstates/')}")


if __name__ == "__main__":
    generate_blockstates()