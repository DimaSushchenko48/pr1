import java.util.concurrent.Semaphore;

class Магазин {
    private int кількістьТоварів; // кількість товару в магазині
    private Semaphore семафор;   // семафор для контролю наявності товарів

    public Магазин(int кількістьТоварів) {
        this.кількістьТоварів = кількістьТоварів;
        this.семафор = new Semaphore(1); // лише один потік може змінювати кількість товарів
    }

    // Метод для додавання товарів в магазин (адміністратор)
    public void додатиТовари(int кількість) {
        try {
            семафор.acquire(); // отримаємо доступ до ресурсу
            кількістьТоварів += кількість;
            System.out.println("Адміністратор додав " + кількість + " товарів. Тепер на складі: " + кількістьТоварів);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            семафор.release(); // звільняємо семафор
        }
    }

    // Метод для покупки товару (покупець)
    public boolean купитиТовар() {
        try {
            семафор.acquire(); // отримаємо доступ до ресурсу
            if (кількістьТоварів > 0) {
                кількістьТоварів--;
                System.out.println("Покупець купив товар. Залишилося на складі: " + кількістьТоварів);
                return true;
            } else {
                System.out.println("Товару немає в наявності.");
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            семафор.release(); // звільняємо семафор
        }
    }

    public int getКількістьТоварів() {
        return кількістьТоварів;
    }
}

class Адміністратор implements Runnable {
    private Магазин магазин;

    public Адміністратор(Магазин магазин) {
        this.магазин = магазин;
    }

    @Override
    public void run() {
        try {
            // Адміністратор додає 10 товарів кожні 3 секунди
            while (true) {
                Thread.sleep(3000);
                магазин.додатиТовари(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Покупець implements Runnable {
    private Магазин магазин;

    public Покупець(Магазин магазин) {
        this.магазин = магазин;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000); // Покупець пробує купити товар кожну секунду
                if (!магазин.купитиТовар()) {
                    // Якщо товару немає в наявності, покупець чекає
                    System.out.println("Покупець чекає...");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

