# jLocalCoin

## Run in local environtment
* `git clone git@gitlab.com:djm.im/jLocalCoin.git`
* `cd jLocalCoin`
* `./gradlew -q run`

### jLocalCoin commands 

```text
help
    - Print help for commands
exit
    - Stop blockchain thread

Wallet commands
wnew [WALLET-NAME]
    - create a new wallet in collection
mwnew [WALLET-NAME-1 ... WALLET-NAME-N]
    - Create N new wallets. If wallet with a name alreaady exist it will be skipped.
wdel [WALLET-NAME]
    - delete a wallet from collection
wstat [WALLET-NAME]
    - Display "balance" for a wallet.
wlist
    - list all wallets with "balances"

Send coin
send [W1-NAME W2-NAME COINS]
    - send from W1 to W2 coins 
```

When NodeCli starts, it creates special miner wallet.  
This wallet is added in collection of wallets with name `_miner`.  

A miner gets reward for each block. The reward is 100 coins.

#### jLocalCoin example
```text
./gradlew -q run

help
wlist

wnew djm
wlist

send _miner djm 100
wlist

wnew aaa
send djm aaa 75
wlist

exit
```

## Other

[jBlockChain](https://gitlab.com/djm.im/jBlockChain) is implementation of blockchain in Java (without transaction).
