package XOlogs;

import java.util.ArrayList;

public class WeaponList {
    ArrayList<Weapon> list= new ArrayList<Weapon>();

    public WeaponList() {
        //ArrayList<Weapon> list

    }
    public void add (Weapon weapon){
        Weapon existedWeapon = findByName(weapon.getName());
        if (existedWeapon!= null)
            existedWeapon.setDamage(existedWeapon.getDamage()+weapon.getDamage());
        else
            list.add(weapon);
    }
    public Weapon findByName (String weaponName){
        if (list!=null)
            for (Weapon weapon:list){
               if (weapon.getName().equals(weaponName))
                  return weapon;
        }
        return null;
    }
    public ArrayList<Weapon> getList(){
        ArrayList<Weapon> newList = new ArrayList<>();
        if (list !=null && !list.isEmpty()) {
            newList.addAll(list);
        }
        return newList;
    }
    public void clear(){
        list.clear();
    }
}
