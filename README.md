<h1 align="center">Stager</h1>
<p align="center">
 <a href="https://github.com/MaxBQb/Stager/releases/latest">
  <img width="32%" src="https://user-images.githubusercontent.com/27343275/182327396-d6fda8fd-f7cf-49bb-a88a-70dc8c061153.png" 
   alt="Stager logo"
   title="Click to open latest release">
 </a>
</p>

## Функционал приложения
Приложение позволяет создавать списки поэтапных задач, делиться с выбранными пользователями текущим статусом задачи, а также получать уведомления об изменении статуса задачи.

#### Поддерживаются Светлая и Тёмная темы, Русский и Английский языки
![image](https://user-images.githubusercontent.com/27343275/182400870-5ada9a08-f9ee-43d1-9eef-f03900d75e95.png)

#### Основной функционал
![image](https://user-images.githubusercontent.com/27343275/182408902-81ab0905-6e65-489f-a844-43599476ed5c.png)




## Как запустить приложение
### Простой способ:
 1. [`>>> СКАЧАТЬ <<<`](../../releases/latest) Stager.apk на телефон (Android 4.1+)
 2. Запустить установку Stager.apk (Требуется разрешить установку из неизвестных источников)
 3. Если у Вас уже установлен Stager, а новая версия не ставится - попробуйте предварительно удалить старую :)

### Сложный способ:
(В данном варианте нет подключения к БД разработчиков, вам нужно будет самим создать свою БД и добавить файл `google-services.json` в проект )
#### 1 вариант:
 1. Установить:
    * [Java](https://www.java.com/)  
    * [Android SDK](https://developer.android.google.cn/studio/releases/platform-tools)
    * [Gradle](https://gradle.org/install/) 
 2. Склонировать репозиторий: 
 ```cmd
 git clone https://github.com/TRPP-IKBO-06/project-three_musketeers.git
 ```
 3. Открыть консоль в директории проекта `project-three_musketeers`:
 ```cmd
 gradlew build
 ``` 
 (Для Windows можете использовать `gradlew.bat`)
 4. Перейти в `project-three_musketeers/app/build/outputs/apk/release` и ввести команду:
 ```cmd
 keytool -genkey -v -keystore stager-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias stager-key
 ```
 5. Ввести команду:
    * Для Windows используйте абсолютный путь к sdk, пример: `D:\Data\sdk\build-tools\30.0.3\`
    ```bash
    zipalign -v -p 4 app-release-unsigned.apk stager-aligned.apk
    ```
 6. Ввести команду:
    * Для Windows используйте абсолютный путь к sdk
    ```cmd
    apksigner sign --ks stager-release-key.jks --out app-release-unsigned.apk stager-aligned.apk
    ```
 7. В папке `app/build/outputs/apk/release` вы найдете `app-release-unsigned.apk`
 8. Пересылаем на устройство, устанавливаем (у вас должна быть разрешена установка из неизвестных источников)!

#### 2 вариант (через Docker):
 1. Скачать [Docker Desktop](https://www.docker.com/products/docker-desktop)
 2. Cклонировать репозиторий: 
 ```cmd
 git clone https://github.com/TRPP-IKBO-06/project-three_musketeers.git
 ``` 
 3. Перейти в папку `project-three_musketeers`
 4. Прописать следующую команду: 
 ```cmd
 docker pull dedicated407/androidstager
 ```
 5. Прописать следующую команду: 
    * Для Windows: 
    ```cmd
    docker run --rm -v "%cd%":/home/gradle/ dedicated407/androidstager sh /home/makeapk
    ```
    * Для Linux: 
    ```bash
    docker run --rm -v "$PWD":/home/gradle/ dedicated407/androidstager sh /home/makeapk
    ```
 6. После работы контейнера в вашей папке появится папка `release`, в ней содержится `stager.apk`, файл с приложением
