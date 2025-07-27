import os
import json

# === CONFIG ===
MODID = "cobblefurnies"
OUTPUT_DIR = "generated"
BLOCKSTATE_DIR = os.path.join(OUTPUT_DIR, "blockstates")
MODEL_DIR = os.path.join(OUTPUT_DIR, "models/block/nightstand")
ITEM_DIR = os.path.join(OUTPUT_DIR, "models/item")
TEMPLATE_DIR = f"{MODID}:block/template"
TEXTURE_BASE = f"{MODID}:block/nightstand"
PARTICLE_BASE = f"{MODID}:block/particles"

# === INPUT ===
with open("woods.json", encoding="utf-8") as f:
    woods = json.load(f)

with open("colors.json", encoding="utf-8") as f:
    colors = json.load(f)

# === HELPERS ===
def save_json(path, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=4)

def get_model_bottom(wood):
    return {
        "parent": f"{TEMPLATE_DIR}/nightstand_bottom",
        "textures": {
            "wood": f"{TEXTURE_BASE}/{wood}",
            "particle": f"{PARTICLE_BASE}/table/{wood}"
        }
    }

def get_model_top_closed(variant, is_color=False):
    tex_path = f"{TEXTURE_BASE}/{variant}"
    particle = f"{PARTICLE_BASE}/color/{variant}" if is_color else f"{PARTICLE_BASE}/table/{variant}"
    return {
        "parent": f"{TEMPLATE_DIR}/nightstand_top_closed",
        "textures": {
            "top": tex_path,
            "particle": particle
        }
    }

def get_model_top_open(variant, is_color=False):
    tex_path = f"{TEXTURE_BASE}/{variant}"
    particle = f"{PARTICLE_BASE}/color/{variant}" if is_color else f"{PARTICLE_BASE}/table/{variant}"
    return {
        "parent": f"{TEMPLATE_DIR}/nightstand_top_open_top",
        "textures": {
            "top": tex_path,
            "particle": particle
        }
    }

def get_item_model(wood):
    return {
        "parent": f"{TEMPLATE_DIR}/nightstand_full",
        "textures": {
            "wood": f"{TEXTURE_BASE}/{wood}"
        }
    }

def generate_blockstate(wood):
    multipart = []
    for facing in ["north", "east", "south", "west"]:
        rotation = {"north": 0, "east": 90, "south": 180, "west": 270}[facing]

        # Bottom
        multipart.append({
            "when": { "facing": facing },
            "apply": {
                "model": f"{MODID}:block/nightstand/{wood}_bottom",
                "y": rotation
            }
        })

        # Top (wood - no concrete)
        multipart.append({
            "when": { "facing": facing, "open": False, "has_concrete": False },
            "apply": {
                "model": f"{MODID}:block/nightstand/{wood}_top_closed",
                "y": rotation
            }
        })
        multipart.append({
            "when": { "facing": facing, "open": True, "has_concrete": False },
            "apply": {
                "model": f"{MODID}:block/nightstand/{wood}_top_open",
                "y": rotation
            }
        })

        # Top (concrete)
        for color in colors:
            multipart.append({
                "when": {
                    "facing": facing,
                    "open": False,
                    "has_concrete": True,
                    "concrete_color": color
                },
                "apply": {
                    "model": f"{MODID}:block/nightstand/{color}_top_closed",
                    "y": rotation
                }
            })
            multipart.append({
                "when": {
                    "facing": facing,
                    "open": True,
                    "has_concrete": True,
                    "concrete_color": color
                },
                "apply": {
                    "model": f"{MODID}:block/nightstand/{color}_top_open",
                    "y": rotation
                }
            })

    return { "multipart": multipart }

# === GENERATION ===
for wood in woods:
    print(f"Generating for wood: {wood}")
    
    # Blockstate
    save_json(os.path.join(BLOCKSTATE_DIR, f"{wood}_nightstand.json"), generate_blockstate(wood))

    # Block models
    save_json(os.path.join(MODEL_DIR, f"{wood}_bottom.json"), get_model_bottom(wood))
    save_json(os.path.join(MODEL_DIR, f"{wood}_top_closed.json"), get_model_top_closed(wood))
    save_json(os.path.join(MODEL_DIR, f"{wood}_top_open.json"), get_model_top_open(wood))

    # Item model
    save_json(os.path.join(ITEM_DIR, f"{wood}_nightstand.json"), get_item_model(wood))

# Generate concrete top models
for color in colors:
    print(f"Generating for concrete color: {color}")
    save_json(os.path.join(MODEL_DIR, f"{color}_top_closed.json"), get_model_top_closed(color, is_color=True))
    save_json(os.path.join(MODEL_DIR, f"{color}_top_open.json"), get_model_top_open(color, is_color=True))
