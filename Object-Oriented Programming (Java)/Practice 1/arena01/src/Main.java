import java.util.Locale;
import java.util.Scanner;

public class Main {

    // Константы игры
    static final int MAX_HERO_HP = 40;
    static final int HEAL_AMOUNT = 8;
    static final int XP_REWARD = 20;
    static final int HERO_DAMAGE = 10;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //Создание персонажа

        // Ввод имени
        String heroName;

        while (true) {
            System.out.print("Введите имя героя: ");

            if (!scanner.hasNextLine()) {
                System.out.println("Ввод завершён.");
                return;
            }

            heroName = scanner.nextLine().strip();

            if (!heroName.isEmpty() && heroName.length() <= 30) {
                break;
            }

            System.out.println("Ошибка: имя должно содержать от 1 до 30 символов.");
        }

        // Ввод класса
        String heroClass;

        while (true) {
            System.out.print("Введите класс (воин, маг, лучник): ");

            if (!scanner.hasNextLine()) {
                System.out.println("Ввод завершён.");
                return;
            }

            heroClass = scanner.nextLine()
                    .strip()
                    .toLowerCase(Locale.ROOT);

            if (heroClass.equals("воин")
                    || heroClass.equals("маг")
                    || heroClass.equals("лучник")) {
                break;
            }

            System.out.println("Ошибка: неизвестный класс.");
        }

        // Ввод уровня
        int level;

        while (true) {
            System.out.print("Введите уровень (1-80): ");

            if (!scanner.hasNext()) {
                System.out.println("Ввод завершён.");
                return;
            }

            if (!scanner.hasNextInt()) {
                System.out.println("Ошибка: уровень должен быть целым числом.");
                scanner.nextLine();
                continue;
            }

            level = scanner.nextInt();
            scanner.nextLine();

            if (level >= 1 && level <= 80) {
                break;
            }

            System.out.println("Ошибка: уровень должен быть от 1 до 80.");
        }

        // Ввод наличия щита
        boolean hasShield;

        while (true) {
            System.out.print("Есть щит? (да/нет): ");

            if (!scanner.hasNextLine()) {
                System.out.println("Ввод завершён.");
                return;
            }

            String shieldInput = scanner.nextLine()
                    .strip()
                    .toLowerCase(Locale.ROOT);

            if (shieldInput.equals("да")) {
                hasShield = true;
                break;
            }

            if (shieldInput.equals("нет")) {
                hasShield = false;
                break;
            }

            System.out.println("Ошибка: введите только «да» или «нет».");
        }

        // Допуск героя

        if (!isAllowed(level, hasShield, heroClass)) {

            // Сначала сообщаем причину по уровню
            if (level < 10) {
                System.out.println("Отказ: недостаточный уровень.");
            }
            // Затем проверяем защиту
            else {
                System.out.println("Отказ: нужна защита — щит или класс маг.");
            }

            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("Допущен: " + heroName + ", " + heroClass);
        System.out.println();

        // Данные противников

        String[] enemyNames = {
                "Крыса",
                "Скелет",
                "Страж"
        };

        int[] enemyHp = {
                12,
                18,
                24
        };

        int[] enemyDamage = {
                4,
                6,
                8
        };

        // Состояние героя

        // Эти переменные должны сохраняться между боями
        int heroHp = MAX_HERO_HP;
        int xp = 0;
        int victories = 0;

        boolean gameInterrupted = false;

        // Три битвы

        for (int i = 0; i < enemyNames.length; i++) {

            // HP текущего противника создаётся заново
            int currentEnemyHp = enemyHp[i];

            // Возможность лечения обновляется в каждом бою
            boolean healUsed = false;

            System.out.println("--------------------------------");
            System.out.println("Раунд " + (i + 1));
            System.out.println("Противник: " + enemyNames[i]);
            System.out.println("--------------------------------");

            // Ходы в битве

            while (heroHp > 0 && currentEnemyHp > 0) {

                System.out.println();
                System.out.println("Герой: " + heroHp + " HP");
                System.out.println(enemyNames[i] + ": " + currentEnemyHp + " HP");

                System.out.println("1 — атаковать");
                System.out.println("2 — лечиться");
                System.out.println("0 — закончить игру");

                System.out.print("Выберите команду: ");

                // Проверяем корректность команды
                if (!scanner.hasNextInt()) {
                    System.out.println("Неизвестная команда.");
                    scanner.nextLine();
                    continue;
                }

                int command = scanner.nextInt();
                scanner.nextLine();

                switch (command) {

                    // Атака
                    case 1:

                        System.out.println(
                                heroName + " атакует " + enemyNames[i]
                                        + " и наносит " + HERO_DAMAGE + " урона."
                        );

                        currentEnemyHp = calculateDamageHp(
                                currentEnemyHp,
                                HERO_DAMAGE
                        );

                        System.out.println(
                                enemyNames[i]
                                        + " теперь имеет "
                                        + currentEnemyHp
                                        + " HP."
                        );

                        // Если противник погиб,
                        // он НЕ отвечает
                        if (currentEnemyHp == 0) {

                            xp += XP_REWARD;
                            victories++;

                            System.out.println(
                                    enemyNames[i] + " побеждён!"
                            );

                            System.out.println(
                                    "Получено XP: " + XP_REWARD
                            );

                            break;
                        }

                        // Если противник выжил,
                        // он отвечает
                        heroHp = calculateDamageHp(
                                heroHp,
                                enemyDamage[i]
                        );

                        System.out.println(
                                enemyNames[i]
                                        + " наносит ответный удар: "
                                        + enemyDamage[i]
                                        + " урона."
                        );

                        System.out.println(
                                "HP героя: " + heroHp
                        );

                        if (heroHp == 0) {
                            System.out.println("Герой погиб.");
                        }

                        break;

                    // Лечение
                    case 2:

                        // Повторное лечение запрещено
                        if (healUsed) {
                            System.out.println(
                                    "Лечение уже использовано в этом бою."
                            );

                            // Повторная попытка не расходует ход
                            continue;
                        }

                        int oldHp = heroHp;

                        heroHp = calculateHealHp(heroHp);
                        healUsed = true;

                        System.out.println(
                                "Герой восстановил "
                                        + (heroHp - oldHp)
                                        + " HP."
                        );

                        System.out.println(
                                "HP героя: " + heroHp
                        );

                        // Лечение расходует ход,
                        // поэтому живой противник отвечает
                        if (currentEnemyHp > 0) {

                            heroHp = calculateDamageHp(
                                    heroHp,
                                    enemyDamage[i]
                            );

                            System.out.println(
                                    enemyNames[i]
                                            + " отвечает и наносит "
                                            + enemyDamage[i]
                                            + " урона."
                            );

                            System.out.println(
                                    "HP героя: " + heroHp
                            );

                            if (heroHp == 0) {
                                System.out.println("Герой погиб.");
                            }
                        }

                        break;

                    // Выход из игры
                    case 0:

                        gameInterrupted = true;

                        System.out.println();
                        System.out.println("Игра прервана.");

                        break;

                    // Неизвестная команда
                    default:

                        System.out.println(
                                "Неизвестная команда. "
                                        + "Введите 1, 2 или 0."
                        );

                        // Состояние игры не меняется
                        break;
                }

                // Если пользователь выбрал выход
                if (gameInterrupted) {
                    break;
                }
            }

            // Проверка состояния после боя

            if (gameInterrupted) {
                break;
            }

            // Если герой погиб,
            // следующие бои не начинаются
            if (heroHp == 0) {
                break;
            }

            // Если противник побеждён
            if (currentEnemyHp == 0) {

                System.out.println();
                System.out.println(
                        "Раунд " + (i + 1)
                                + ": победа, HP героя "
                                + heroHp
                                + ", XP "
                                + xp
                );
            }
        }

        // Итог

        printResult(
                heroName,
                "А — Руины",
                victories,
                xp,
                heroHp,
                gameInterrupted
        );

        scanner.close();
    }

    // Проверка допуска

    static boolean isAllowed(
            int level,
            boolean hasShield,
            String heroClass
    ) {
        return level >= 10
                && (hasShield || heroClass.equals("маг"));
    }

    // Расчет Hp после боя

    static int calculateDamageHp(
            int currentHp,
            int damage
    ) {
        return Math.max(0, currentHp - damage);
    }

    // Рассчет Hp после лечения

    static int calculateHealHp(int currentHp) {
        return Math.min(
                MAX_HERO_HP,
                currentHp + HEAL_AMOUNT
        );
    }

    // Вывод итога

    static void printResult(
            String heroName,
            String variant,
            int victories,
            int xp,
            int heroHp,
            boolean gameInterrupted
    ) {
        String result;

        if (gameInterrupted) {
            result = "Игра прервана";
        } else if (heroHp == 0) {
            result = "Поражение";
        } else if (victories == 3) {
            result = "Арена пройдена";
        } else {
            result = "Игра завершена";
        }

        System.out.println();
        System.out.println("--------------------------------");
        System.out.println("ИТОГ");
        System.out.println("--------------------------------");
        System.out.println("Имя: " + heroName);
        System.out.println("Вариант: " + variant);
        System.out.println("Побед: " + victories);
        System.out.println("XP: " + xp);
        System.out.println("HP: " + heroHp);
        System.out.println("Итог: " + result);
        System.out.println("--------------------------------");
    }
}