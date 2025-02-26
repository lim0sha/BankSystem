package Presentation.Console;


import Presentation.Interfaces.IMenu;

public class Main {
    public static void main(String[] args) {
        IMenu menu = new Menu();
        menu.Run();
    }
}
