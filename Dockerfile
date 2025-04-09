#________            _________                ________              ______      ______  ___     ______ ___________          __________
#__  ___/________   ___  ____/____________ ______  __ )_____ __________  /__    ___   |/  /________  /____(_)__  /____      ___  ____/_____ ______________ ___
#_____ \_  __ \_ | / /  /    _  __ \_  __ `__ \_  __  |  __ `/_  __ \_  //_/    __  /|_/ /_  __ \_  __ \_  /__  /_  _ \     __  /_   _  __ `/_  ___/_  __ `__ \
#____/ // /_/ /_ |/ // /___  / /_/ /  / / / / /  /_/ // /_/ /_  / / /  ,<       _  /  / / / /_/ /  /_/ /  / _  / /  __/     _  __/   / /_/ /_  /   _  / / / / /
#____/ \____/_____/ \____/  \____//_/ /_/ /_//_____/ \__,_/ /_/ /_//_/|_|      /_/  /_/  \____//_.___//_/  /_/  \___/      /_/      \__,_/ /_/    /_/ /_/ /_/

#=====================
# Debian + JDK17
#=====================
FROM openjdk:17-ea-slim-buster

ENV DEBIAN_FRONTEND=noninteractive

#=====================================================
# Обновляемся и инсталируем необходимые нам библиотеки
#=====================================================
RUN apt-get -qqy update
RUN apt-get -qqy --no-install-recommends install ca-certificates tzdata zip unzip curl wget libqt5webkit5 libgconf-2-4 xvfb gnupg sudo procps xvfb usbutils \
&& rm -rf /var/lib/apt/lists/*

#========================
# Устанавливаем Тайм Зону
#========================
ENV TZ = "Asia/Vladivostok"
RUN echo "${TZ}" > /etc/timezone \
  && dpkg-reconfigure --frontend noninteractive tzdata

#==========================================
# Создаём пользователя и рабочую директорию
#==========================================
ARG USER_PASS=secret
RUN groupadd fermausr \
         --gid 1301 \
  && useradd fermausr \
         --uid 1300 \
         --gid 1301 \
         --create-home \
         --shell /bin/bash \
  && usermod -aG sudo fermausr \
  && echo fermausr:${USER_PASS} | chpasswd \
  && echo 'fermausr ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers

WORKDIR /home/fermausr

#======================
# Добавляем Android SDK
#======================
ENV SDK_VERSION=commandlinetools-linux-8512546_latest
ENV ANDROID_BUILD_TOOLS_VERSION=34.0.0
ENV ANDROID_FOLDER_NAME=cmdline-tools
ENV ANDROID_ADB_SERVER_ADDRESS=host.docker.internal
ENV ANDROID_DOWNLOAD_PATH=/home/fermausr/${ANDROID_FOLDER_NAME} \
    ANDROID_HOME=/opt/android \
    ANDROID_TOOL_HOME=/opt/android/${ANDROID_FOLDER_NAME}

RUN wget -O tools.zip https://dl.google.com/android/repository/${SDK_VERSION}.zip && \
    unzip tools.zip && rm tools.zip && \
    chmod a+x -R ${ANDROID_DOWNLOAD_PATH} && \
    chown -R 1300:1301 ${ANDROID_DOWNLOAD_PATH} && \
    mkdir -p ${ANDROID_TOOL_HOME} && \
    mv ${ANDROID_DOWNLOAD_PATH} ${ANDROID_TOOL_HOME}/tools
ENV PATH=$PATH:${ANDROID_TOOL_HOME}/tools:${ANDROID_TOOL_HOME}/tools/bin

RUN mkdir -p ~/.android && \
    touch ~/.android/repositories.cfg && \
    echo y | sdkmanager "platform-tools" && \
    echo y | sdkmanager "build-tools;$ANDROID_BUILD_TOOLS_VERSION" && \
    mv ~/.android .android && \
    chown -R 1300:1301 .android
ENV PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools

#===========================
# Ставим nodejs, npm, appium
#===========================
ENV NODE_VERSION=20
ENV APPIUM_VERSION=2.11.3
RUN curl -sL https://deb.nodesource.com/setup_${NODE_VERSION}.x | bash && \
    apt-get -qqy install nodejs && \
    npm init && \
    npm install -g appium@${APPIUM_VERSION} --loglevel verbose && \
    exit 0 && \
    npm cache clean && \
    apt-get remove --purge -y npm && \
    apt-get autoremove --purge -y && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* && \
    apt-get clean

#=============
# Чиним доступ
#=============
RUN chown -R 1300:1301 /usr/lib/node_modules/appium

#==========================
# Копируем стартовый скрипт
#==========================
ENV SCRIPT_PATH="appium-docker"
RUN mkdir -p ${SCRIPT_PATH}
COPY start.sh \
     ${SCRIPT_PATH}/
RUN chown -R 1300:1301 ${SCRIPT_PATH}
ENV APP_PATH=/home/fermausr/${SCRIPT_PATH}

#====================
# Используем пользюка
#====================
USER 1300:1301

#=========================================
# Устанавливаем драйвера для Android и IoS
#=========================================
ENV APPIUM_DRIVER_UIAUTOMATOR2_VERSION="3.7.7"
#ENV APPIUM_DRIVER_XCUITEST_VERSION="7.24.15"

RUN appium driver install uiautomator2@${APPIUM_DRIVER_UIAUTOMATOR2_VERSION}
#appium driver install xcuitest@${APPIUM_DRIVER_XCUITEST_VERSION}

#================================
# Устанавливаем плагины для фермы
#================================
ENV DEVICE_FARM=9.1.4
ENV DASHBOARD=2.0.3

RUN appium plugin install --source=npm appium-device-farm@${DEVICE_FARM} && \
    appium plugin install --source=npm appium-dashboard@${DASHBOARD}

#================
# Открываем порты
#---------------
# 4723 Appium
#================
EXPOSE 4723

#=================
# Запускаем Скрипт
#=================
CMD ./${SCRIPT_PATH}/start.sh
