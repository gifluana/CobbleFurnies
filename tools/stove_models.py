import os
import json

def get_blockstate(color):
    """Generate blockstate JSON for a stove color with proper pot rotation, lid and cooking"""

    multipart = []

    pot_colors = ["red", "yellow", "white", "pink", "green", "blue", "black"]

    # Pot + lid + broth
    for pot_color in pot_colors:
        for facing, rotation in [("north", None), ("east", 90), ("south", 180), ("west", 270)]:
            apply_base = {
                "model": f"cobblefurnies:block/stove/pot_{pot_color}"
            }
            if rotation is not None:
                apply_base["y"] = rotation

            apply_open = {
                "model": f"cobblefurnies:block/stove/pot_open_{pot_color}"
            }
            if rotation is not None:
                apply_open["y"] = rotation

            apply_broth = {
                "model": f"cobblefurnies:block/stove/pot_broth_{pot_color}"
            }
            if rotation is not None:
                apply_broth["y"] = rotation

            # Tampa FECHADA → pot_<color>
            multipart.append({
                "when": {
                    "facing": facing,
                    "has_pot": "true",
                    "pot_color": pot_color,
                    "lid": "true"
                },
                "apply": apply_base
            })

            # Tampa ABERTA → pot_open_<color>
            multipart.append({
                "when": {
                    "facing": facing,
                    "has_pot": "true",
                    "pot_color": pot_color,
                    "lid": "false"
                },
                "apply": apply_open
            })

            # Cozinhando → overlay do caldo
            multipart.append({
                "when": {
                    "facing": facing,
                    "has_pot": "true",
                    "pot_color": pot_color,
                    "cooking": "true"
                },
                "apply": apply_broth
            })

    # Top models (facing only)
    for facing, rotation in [("north", None), ("east", 90), ("south", 180), ("west", 270)]:
        apply_dict = {"model": f"cobblefurnies:block/stove/{color}_top"}
        if rotation is not None:
            apply_dict["y"] = rotation

        multipart.append({
            "when": {"facing": facing},
            "apply": apply_dict
        })

    # Bottom models (facing + connected states)
    bottom_types = [
        ("middle", "true", "true"),
        ("left", "true", "false"),
        ("right", "false", "true"),
        ("", "false", "false")  # standalone
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