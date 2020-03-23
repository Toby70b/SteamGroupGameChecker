#Dockerfile.dev:

# base image
FROM node:12.2.0-alpine

# set working directory
WORKDIR ./

# add `//node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

COPY package.json ./package.json

#install dependencies:
RUN npm install --silent
RUN npm install react-scripts@3.0.1 -g


COPY . .

# start
CMD ["npm", "start"]
