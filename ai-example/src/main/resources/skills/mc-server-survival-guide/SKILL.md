---
name: "mc-server-survival-guide"
description: "Minecraft server survival guide, documenting survival techniques and item acquisition methods from scratch. Invoke when user asks about MC survival tips or how to obtain items."
---

# MC Server Survival Guide

This skill documents survival techniques and item acquisition methods from scratch on a Minecraft server.

## Prerequisites

### 1. Install MCP Server

Configure Minecraft MCP Server in Trae IDE with the following settings:

```json
{
  "mcpServers": {
    "minecraft": {
      "command": "npx",
      "args": [
        "-y",
        "github:yuniko-software/minecraft-mcp-server",
        "--host",
        "localhost",
        "--port",
        "25565",
        "--username",
        "ClaudeBot"
      ]
    }
  }
}
```

**Configuration Details**:
- `--host`: Minecraft server address
- `--port`: Server port (default: 25565)
- `--username`: Bot username

### 2. In-Game Requirements
- Empty hand (no tools needed)

## Available MCP Skills

### Movement & Position
| Skill | Description | Parameters |
|-------|-------------|------------|
| `get-position` | Get current bot position | None |
| `move-to-position` | Move to specified coordinates | `x`, `y`, `z`, `range`, `timeoutMs` |
| `move-in-direction` | Move in specified direction | `direction` (forward/back/left/right), `duration` |
| `look-at` | Make bot look at specified position | `x`, `y`, `z` |
| `jump` | Jump | None |
| `fly-to` | Fly to specified position | `x`, `y`, `z` |

### Items & Inventory
| Skill | Description | Parameters |
|-------|-------------|------------|
| `list-inventory` | List all inventory items | None |
| `find-item` | Find item in inventory | `nameOrType` |
| `equip-item` | Equip specified item | `itemName`, `destination` |

### Block Operations
| Skill | Description | Parameters |
|-------|-------------|------------|
| `dig-block` | Dig block at specified position | `x`, `y`, `z` |
| `place-block` | Place block at specified position | `x`, `y`, `z`, `faceDirection` |
| `get-block-info` | Get block information | `x`, `y`, `z` |
| `find-blocks` | Find nearby blocks | `blockType`, `maxDistance`, `count` |

### Crafting & Smelting
| Skill | Description | Parameters |
|-------|-------------|------------|
| `craft-item` | Craft item | `outputItem`, `amount` |
| `can-craft` | Check if item can be crafted | `itemName` |
| `get-recipe` | Get item crafting recipe | `itemName` |
| `list-recipes` | List available crafting recipes | `outputItem` |
| `smelt-item` | Smelt item | `x`, `y`, `z`, `inputItem`, `fuelItem`, etc. |

### Interaction & Communication
| Skill | Description | Parameters |
|-------|-------------|------------|
| `send-chat` | Send chat message | `message` |
| `read-chat` | Read recent chat messages | `count` |
| `find-entity` | Find nearby entities | `type`, `maxDistance` |
| `detect-gamemode` | Detect current game mode | None |

## Stone Pickaxe Guide

The stone pickaxe is an essential basic tool in survival mode. Here are the complete steps to obtain it:

### Step 1: Collect Logs

1. Use `find-blocks` to locate nearby trees:
   - Search for `spruce_log` or `oak_log`
   - Set `maxDistance` to 16 blocks

2. Use `dig-block` to collect logs:
   - Need to be close to the tree (3-5 blocks) to dig successfully
   - Collect at least 4 logs

### Step 2: Craft Planks

Use `craft-item` to craft planks:
- Output item: `spruce_planks` or `oak_planks`
- Quantity: 4 (1 log = 4 planks)

### Step 3: Craft Sticks

Use `craft-item` to craft sticks:
- Output item: `stick`
- Quantity: 1 (yields 4 sticks)

### Step 4: Craft Crafting Table

Use `craft-item` to craft a crafting table:
- Output item: `crafting_table`
- Requires: 4 planks
- Quantity: 1

### Step 5: Craft Wooden Pickaxe

Use `craft-item` to craft a wooden pickaxe:
- Output item: `wooden_pickaxe`
- Requires: 3 planks + 2 sticks
- Quantity: 1

### Step 6: Mine Stone to Get Cobblestone

1. Use `equip-item` to equip the wooden pickaxe
2. Use `find-blocks` to find nearby stone (`stone`)
3. Use `dig-block` to mine the stone
4. **Important**: Cobblestone drops on the ground after mining!
5. Use `move-to-position` to move to the mining location to pick up drops
6. Mine at least 3 stone blocks to get 3 cobblestone

### Step 7: Craft Stone Pickaxe

1. Use `move-to-position` to move near the crafting table
2. Use `craft-item` to craft the stone pickaxe:
   - Output item: `stone_pickaxe`
   - Requires: 3 cobblestone + 2 sticks
   - Quantity: 1

## Important Notes

1. **Pickup Drops**: After mining blocks, drops fall on the ground - you must move to the drop location to pick them up
2. **Crafting Table Location**: Need to be near a crafting table (about 3-5 blocks) to craft advanced items
3. **Tool Durability**: Wooden pickaxe has low durability, upgrade to stone pickaxe as soon as possible
4. **Mining Speed**: Being closer to target blocks increases mining speed

## Common Crafting Recipes

| Item | Materials | Quantity |
|------|-----------|----------|
| Planks | 1 Log | 4 |
| Sticks | 2 Planks | 4 |
| Crafting Table | 4 Planks | 1 |
| Wooden Pickaxe | 3 Planks + 2 Sticks | 1 |
| Stone Pickaxe | 3 Cobblestone + 2 Sticks | 1 |
| Iron Pickaxe | 3 Iron Ingots + 2 Sticks | 1 |
| Diamond Pickaxe | 3 Diamonds + 2 Sticks | 1 |
