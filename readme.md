
# Small Ships 1.21.8 port

### Todo before stable
- Proper test on all platform, all features, sp and mp
- Fix all stuff that is not working, including data transfer
- Possible add some nice features like fuze & reload sound for ship-cannons etc

### Not working in 1.21.8
(all major functions should be working now, however saved nbt data, especially shields wont transfer from older versions)
- Some config stuff might be broken (SmallShipsConfigImpl)
- Data transfer from older version, chest data & sheilds
    - `I will need to revisit this but it was annoying because the way the values are received are not the same anymore, and they are not compatible with each other`


#### Does not seam to work in 1.21.4 but should
- Dye color of cannon shot
  - Also apply this for ship-cannons, but maybe use the ship dye color?

#### Possible improvements
- No reload sound when ready to fire again for ship-cannons
    - same with fuze found
- Banner not dropped when destroyed
- Sound when adding banner and cannons
