import os
import json
from PIL import Image

def extract_colors(image_path):
    """Extracts exactly 7 colors from a given image from left (darkest) to right (lightest)."""
    img = Image.open(image_path).convert("RGB")
    width, height = img.size

    # Define 7 evenly spaced positions to sample colors
    step = width // 7
    sampled_colors = [img.getpixel((step * i + step // 2, height // 2)) for i in range(7)]
    
    # Convert to hex format
    hex_colors = [f"#{r:02x}{g:02x}{b:02x}" for r, g, b in sampled_colors]
    return hex_colors

def generate_palette_json(colors_folder, output_json):
    """Generate a JSON file mapping color names to extracted palettes."""
    palette_data = {}
    
    for file in sorted(os.listdir(colors_folder)):
        if file.endswith(".png") or file.endswith(".jpg"):
            color_name = os.path.splitext(file)[0]  # Extract color name from filename
            image_path = os.path.join(colors_folder, file)
            palette_data[color_name] = extract_colors(image_path)
    
    with open(output_json, "w", encoding="utf-8") as f:
        json.dump(palette_data, f, indent=4)
    
    print(f"Palette JSON saved to {output_json}")

# Example usage
colors_folder = "colors"  # Folder containing palette images
output_json = "wood_palette.json"  # Output JSON file
generate_palette_json(colors_folder, output_json)
