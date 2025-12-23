# Stage 1: Build angular app
FROM node:20-alpine AS build

ARG RECAPTCHA_SITE_KEY
ARG GITHUB_URL
ARG LINKEDIN_URL
ARG LINKURTO_SERVER_URL
ARG LINKURTO_FRONT_URL

WORKDIR /app

# Instala as dependências primeiro para aproveitar o cache do Docker
COPY frontend/package*.json ./
RUN npm install

# Copia o restante dos arquivos
COPY frontend/ ./

# Cria o arquivo environment.ts e environment.development.ts a partir do exemplo e substitui os placeholders
# Obs.: estou usando os mesmos valores de prod em dev por se tratar de uma aplicação que não vai para produção
RUN cp src/environments/environment.example.ts src/environments/environment.ts && \
    cp src/environments/environment.example.ts src/environments/environment.development.ts && \
    sed -i "s|RECAPTCHA_SITE_KEY|$RECAPTCHA_SITE_KEY|g" src/environments/environment.ts && \
    sed -i "s|RECAPTCHA_SITE_KEY|$RECAPTCHA_SITE_KEY|g" src/environments/environment.development.ts && \
    sed -i "s|GITHUB_URL|$GITHUB_URL|g" src/environments/environment.ts && \
    sed -i "s|GITHUB_URL|$GITHUB_URL|g" src/environments/environment.development.ts && \
    sed -i "s|LINKEDIN_URL|$LINKEDIN_URL|g" src/environments/environment.ts && \
    sed -i "s|LINKEDIN_URL|$LINKEDIN_URL|g" src/environments/environment.development.ts && \
    sed -i "s|LINKURTO_SERVER_URL|$LINKURTO_SERVER_URL|g" src/environments/environment.ts && \
    sed -i "s|LINKURTO_SERVER_URL|$LINKURTO_SERVER_URL|g" src/environments/environment.development.ts && \
    sed -i "s|LINKURTO_FRONT_URL|$LINKURTO_FRONT_URL|g" src/environments/environment.ts && \
    sed -i "s|LINKURTO_FRONT_URL|$LINKURTO_FRONT_URL|g" src/environments/environment.development.ts

# Builda o projeto em modo desenvolvimento
RUN npm run build -- --configuration development

# Stage 2: Serve app with nginx
FROM nginx:stable-alpine

# Remove arquivos padrão do nginx
RUN rm -rf /usr/share/nginx/html/*

# Copia a configuração customizada do nginx para o diretório de templates
COPY docker/frontend/nginx.conf /etc/nginx/templates/default.conf.template

# Copia o build do estágio anterior
# Nota: No Angular 18+, o output geralmente vai para dist/frontend/browser
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
