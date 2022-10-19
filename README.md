## Glowstick: Emissive Textures via JSON, for Quilt Loader + Quilted Fabric API (1.19.2+)
Glowstick is a mod that adds a simplistic emissive/'glowing' textures feature to vanilla's generated item textures (nearly all items that use a set of texture layers).

Currently (as of 0.1.0), *only item models are supported*, and armor overlay support is planned for the near future.

Tested to be compatible with [Chime](https://github.com/emilyploszaj/chime)'s item predicate extensions.

### How to use Glowstick:

In the main json object for an item model, add an array object with the name `"emissive_texures"`, and put the *texture ids* (not the layer names themselves!) for which you want the emissive effect to apply to within.

Example: (at `assets/minecraft/models/item/stick.json`)
```
{
  "parent": "minecraft:item/handheld",
  "textures": {
    "layer0": "testpack:item/glowstick",
    "layer1": "testpack:item/glowstick2"
  },
  "emissive_textures": [
    "testpack:item/glowstick2"
  ]
}
```
### License:
Glowstick is licensed under the *Mozilla Public License, Version 2.0* (see the LICENSE file and the licensing header!)

