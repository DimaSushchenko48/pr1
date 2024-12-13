public class ІнтернетМагазин {
    public static void main(String[] args) {
        Магазин магазин = new Магазин(0); // Початково немає товарів

        // Створення і запуск потоків
        Thread адміністратор = new Thread(new Адміністратор(магазин));
        Thread покупець1 = new Thread(new Покупець(магазин));
        Thread покупець2 = new Thread(new Покупець(магазин));

        адміністратор.start();
        покупець1.start();
        покупець2.start();
    }
}
