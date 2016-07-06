package net.doodcraft.dooder07.spigot.doodcore.features;

import org.bukkit.event.Listener;

/**
 * The MIT License (MIT)
 * -
 * Copyright (c) 2016 Conor O'Shields
 * -
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * -
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * -
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class EasterEgg implements Listener {

//    int bGrantedArrow = 0;
//
//    public static Recipe getExplosiveArrowRecipe() {
//        ItemStack explosiveArrow = getExplosiveArrow();
//        ShapedRecipe recipe = new ShapedRecipe(explosiveArrow);
//        recipe.shape(new String[] { "***", "*a*", "***" }).setIngredient('*', Material.SULPHUR).setIngredient('a', Material.ARROW);
//        return (Recipe) recipe;
//    }
//
//    @EventHandler
//    public void onEntityShootBow(EntityShootBowEvent event) {
//        this.bGrantedArrow = 0;
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onRename(PrepareAnvilEvent event) {
//        if (event.getInventory().contains(getExplosiveArrow())) {
//            event.setResult(getExplosiveArrow());
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onHit(ProjectileHitEvent event) {
//        if (event.getEntityType() == EntityType.ARROW) {
//            Arrow arrow = (Arrow) event.getEntity();
//
//            if (event.getEntity().getShooter() instanceof Player) {
//
//                Player player = (Player) event.getEntity().getShooter();
//
//                if (player.getInventory().containsAtLeast(getExplosiveArrow(), 1)) {
//
//                    player.getInventory().remove(getExplosiveArrow());
//                    arrow.getWorld().createExplosion(arrow.getLocation(), 1.25f);
//                    arrow.remove();
//                }
//            }
//        }
//    }
//
//    @EventHandler(priority=EventPriority.HIGH)
//    public void onPlayerItemHeld(PlayerItemHeldEvent event){
//        Player p = event.getPlayer();
//        if (this.bGrantedArrow == 1) {
//
//            if (p.getGameMode() != GameMode.CREATIVE) {
//                p.getInventory().removeItem(new ItemStack(Material.ARROW, 1));
//            }
//            this.bGrantedArrow = 0;
//        }
//    }
//
//    @EventHandler(priority=EventPriority.HIGH)
//    public void onPlayerUse(PlayerInteractEvent event){
//        Player p = event.getPlayer();
//
//        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) &&
//                this.bGrantedArrow == 0 && // Sometimes we get multiple calls to onPlayerUse, we're only interested in the first.
//                p.getItemInHand().getType() == Material.BOW &&
//                p.getGameMode() != GameMode.CREATIVE && // No need for arrows in inventory in creative mode
//                p.getInventory().containsAtLeast(getExplosiveArrow(), 1)) // We need at least one blaze rod in inventory for explosive arrows.
//        {
//            p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
//            this.bGrantedArrow = 1;
//        }
//    }
//
//    public static ItemStack getExplosiveArrow() {
//        ItemStack arrow = new ItemStack(Material.BLAZE_ROD);
//        ItemMeta arrowMeta = arrow.getItemMeta();
//        arrowMeta.setDisplayName(StringParser.addColor("&cExplosive Arrow"));
//        arrowMeta.addEnchant(Enchantment.SILK_TOUCH, 69, true);
//        arrowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//        arrow.setItemMeta(arrowMeta);
//        return arrow;
//    }
}
