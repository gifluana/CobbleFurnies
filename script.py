import json
import os

# Wood types to generate
wood_types = ["oak", "spruce", "dark_oak"]

# Blockstate JSON template (with `{wood}` placeholder)
template = {
    "variants": {
        f"facing={facing},open={open_state},hinge={hinge}": (
            {
                "model": f"cobblefurnies:block/cabinet/{{wood}}_{'open' if open_state == 'true' else 'closed'}_{hinge}"
            }
            if facing == "north" else {
                "model": f"cobblefurnies:block/cabinet/{{wood}}_{'open' if open_state == 'true' else 'closed'}_{hinge}",
                "y": {"east": 90, "south": 180, "west": 270}[facing]
            }
        )
        for facing in ["north", "east", "south", "west"]
        for open_state in ["false", "true"]
        for hinge in ["left", "right"]
    }
}

# Output directory
output_dir = "generated_blockstates"
os.makedirs(output_dir, exist_ok=True)

# Generate JSON files
for wood in wood_types:
    data = json.loads(json.dumps(template).replace("{wood}", wood))
    with open(os.path.join(output_dir, f"{wood}.json"), "w") as f:
        json.dump(data, f, indent=4)

print(f"Generated blockstates for: {', '.join(wood_types)}")
