
# Small Ships 1.21.8 port

### Not working in 1.21.8
- Ship GUI
- Controlling ground cannon
  - And probably some things that will only be found once this is fixed
- Zooming in F5 mode
    - `Mixing issues; need to compare old MC code to new MC to to figure out what is going on and where to mixin`
- Shields
    - `ListTag not compatible with new data save structure, probobly needs a rewrite of how data is saved`


#### Does not seam to work in 1.21.4 but should
- Dye color of cannon shot
  - Also apply this for ship-cannons, but maybe use the ship dye color?
- Check the player shoot code, right now your velocity just stops after a while
- No reload sound when ready to fire again for ship-cannons
  - same with fuze found

