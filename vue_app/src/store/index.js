import Vue from 'vue'
import Vuex from 'vuex'
import Phaser from 'phaser'

Vue.use(Vuex);

import {get} from '../helper/request'
import {onMessage} from '../helper/socket'
import Action from '../helper/game_actions'

const store = new Vuex.Store({
  state: {
    user: null,
    playedGames: [],
    socketClient: null,
    playingGame: null,
    games: [],
    onlineUsers: [],
    correctWords: [],
    haveInvitation: false,
    haveCorrectWords: false,
    invitation: [],
    isDeclined: false,
    currentComponent: 'welcome-screen',
    error: null,
    gameAnimation: new Phaser.Game(1140, 665, Phaser.AUTO, 'content', {
      preload() {
        this.load.atlas('knight', './assets/sprites/knight.png', './assets/sprites/knight.json');
        this.load.atlas('girl', './assets/sprites/girl.png', './assets/sprites/girl.json');
        this.load.atlas('wizard', './assets/sprites/wizard.png', './assets/sprites/wizard.json');
        this.load.atlas('archer', './assets/sprites/archer.png', './assets/sprites/archer.json');
      },
      create() {
        this.state.add('initial', {
          preload() {
          },
          create() {
          },
          update() {
          }
        });

        this.state.start('initial');
      }
    }, true),
  },
  mutations: {
    invite(state, invitation) {
      state.haveInvitation = true;
      state.invitation = invitation;
    },
    saveUser(state, user) {
      state.user = user;
    },
    setPlayedGames(state, games) {
      state.playedGames = games;
    },
    saveCorrectWords(state, data) {
      state.haveCorrectWords = true;
      state.correctWords = data;
    },
    setSocketClient(state, socketClient) {
      state.socketClient = socketClient;
    },
    reset(state) {
      state.user = null;
      state.socketClient = null;
      state.playingGame = null;
      state.games = [];
      state.onlineUsers = [];
      state.haveInvitation = false;
      state.invitation = [];
      state.isDeclined = false;
      state.currentComponent = 'welcome-screen';
      state.error = null;
    },
    error(state, error) {
      state.error = error;
    },
    resetPlayingGame(state) {
      state.playingGame = null;
    },
    setPlayingGame(state, game) {
      state.playingGame = game;
    },
    setPlayingGameMode(state, mode) {
      state.playingGame.mode = mode;
    },
    setPlayingGameGuest(state, guest) {
      state.playingGame.guest = guest;
    },
    setMasterCharacter(state, character) {
      state.playingGame.master.character = character;
    },
    setGuestCharacter(state, character) {
      state.playingGame.guest.character = character;
    },
    setGameCharacter(state, character) {
      if (state.user.name === state.playingGame.master.name) {
        state.playingGame.master.character = character;
      } else if (state.user.name === state.playingGame.guest.name) {
        state.playingGame.guest.character = character;
      }
    },
    setRivalCharacter(state, character) {
      if (state.user.name === state.playingGame.master.name) {
        state.playingGame.guest.character = character;
      } else if (state.user.name === state.playingGame.guest.name) {
        state.playingGame.master.character = character;
      }
    },
    setGameStatus(state, status) {
      state.playingGame.status = status;
    },
    setGameTimeLeft(state, timeLeft) {
      state.playingGame.timeLeft = timeLeft;
    },
    setGameTopic(state, question) {
      state.playingGame.topic = question;
    },
    updateMasterCharacterInfo(state, info) {
      state.playingGame.master.character.updateState(info);
    },
    updateGuestCharacterInfo(state, info) {
      state.playingGame.guest.character.updateState(info);
    },
    setListGame(state, games) {
      state.games = games;
    },
    addAnswer(state, answer) {
      if (state.user.name === state.playingGame.master.name) {
        state.playingGame.master.character.answers.push(answer);
      } else if (state.user.name === state.playingGame.guest.name) {
        state.playingGame.guest.character.answers.push(answer);
      }
    },
    setOnlineUser(state, onlineUsers) {
      state.onlineUsers = onlineUsers;
    },
    setCurrentComponent(state, component) {
      state.currentComponent = component;
    }
  },
  actions: {
    fetchUser({commit}) {
      return new Promise((res, rej) => {
        get('/api/user')
          .then(({data}) => {
            //this.$store.user.id++;
            commit('saveUser', data);
            res();
          })
          .catch(() => {
            rej();
          })
      })
    },
    connectSocket({commit}, {userName, password, avatar}) {
      return new Promise((res, rej) => {
        let endPointURL = `ws://${window.location.host}/game-server/${userName}/${password}/${avatar}`;
        let socketClient = new WebSocket(endPointURL);
        socketClient.onopen = function () {
          socketClient.onmessage = onMessage;
          res();
        };
        socketClient.onerror = function () {
          commit('error', 'Some error occurred');
          socketClient.close();
        };
        socketClient.onclose = function () {
          commit('reset');
          commit('setCurrentComponent', 'welcome-screen');
        };
        commit('setSocketClient', socketClient);
      });
    },
    fetchListGame({state}) {
      state.socketClient.send(JSON.stringify({action: Action.GET_LIST_GAME}));
    }
  }
});

export default store;
