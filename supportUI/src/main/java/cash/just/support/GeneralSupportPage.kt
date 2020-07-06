@file:Suppress("UNCHECKED_CAST", "UNUSED")
package cash.just.support

enum class GeneralSupportPage(private val title: String, private val description: String) : BaseSupportPage {
    GET_STARTED("How do I get started?",
            "Welcome to the Coinsquare Wallet!\n\nThe Coinsquare Wallet app connects directly to the bitcoin network. There are no accounts or usernames with the Coinsquare Wallet app. Instead, you will have a \"wallet\" where you will store your money. It's just like the physical wallet you put your cash in, except that it exists only on the internet and holds digital money.\n" +
            "\n" +
            "If this is your first time using the Coinsquare Wallet App, simply choose \"Get Started\" to start.\n" +
            "\n" +
            "If you have used the Coinsquare Wallet app before and would like to use the same wallet, choose \"Restore Wallet\" and prepare to enter the Recovery Key for the wallet you would like to restore."),
    SEND("How do I send bitcoin?","To send money to someone you need to enter their bitcoin address. There are three different ways to enter this information depending on how you received it:\n" +
            "\n" +
            "\t\t - If you are presented with a QR code, press the \"scan\" button and scan the QR code.\n" +
            "\t\t - If you receive an address through email or text, copy the address to your device's clipboard and press the \"paste\" button.\n" +
            "\t\t - If you receive the address another way, tap the \"To\" label and use the keyboard to enter the address.\n" +
            "\t\t - Once you have entered the payment address, tap the \"Amount\" label and enter the amount you wish to send. The button to the right will allow you to toggle the view between the amount of bitcoin to be sent or another currency.\n" +
            "\n" +
            "\t\t - If you would like to save a note about this transaction, tap the \"Memo\" label. This note will only be visible to you, and will not be sent together with the transaction.\n" +
            "\n" +
            "Important: If bitcoin is sent to the wrong address, it can not be refunded. When sending bitcoin, always check to make sure the address you are sending to is the same as the one that was given to you (comparing the first 5 or 6 digits is usually sufficient to make sure you are using the correct address)."),
    RECEIVE("How do I receive bitcoin?","If you would like to receive bitcoin from someone, you will need to give them your bitcoin address. There are a few ways to do this:\n" +
            "\n" +
            "\t\t - Show them the \"Receive Money\" screen in your Coinsquare Wallet App, and let them scan your QR code.\n" +
            "\t\t - Press the \"Share\" button under the QR code to send them your address via email or text message.\n" +
            "\t\t - Tap the QR code to copy your address to your device's memory. You can now paste your address into a website or other app.\n" +
            "\t\t - The bitcoin address shown on your receive address will change every time you receive money, but old addresses will continue to work.\n" +
            "\n" +
            "Important: If bitcoin is sent to the wrong address, it can not be refunded. When sharing your address, always check to make sure the address you give out is the same as the one shown under your QR code (comparing the first 5 or 6 digits is usually sufficient to make sure you are using the correct address)."),
    IMPORT_WALLET("How do I import a bitcoin paper wallet?","If you receive a QR code labeled as a \"paper wallet\" or \"private key\", you can import it into your Coinsquare app's Bitcoin wallet by following these steps:\n\n" +
            "\t\t 1. On the main screen, tap on Menu.\n" +
            "\t\t 2. Under Preferences, tap on Bitcoin Settings.\n" +
            "\t\t 3. Select Redeem Private Key.\n" +
            "\t\t 4. Tap on Scan Private Key and scan the QR code on your paper wallet.\n" +
            "\t\t 5. Any funds stored on the paper wallet/private key will be sent into your Coinsquare app's Bitcoin wallet, and will not remain on the paper wallet/private key.\n" +
            "\n\n" +
            "NOTE:  If you are importing a bitcoin paper wallet created before the August 1, 2017 fork, and would like to get the bitcoin cash credit, follow the steps here.\n" +
            "\n\n" +
            "If you want to import a wallet created with the Coinsquare Wallet App, use the Unlink from this device function instead."),
    PIN("Why do I need a PIN?","\t\tThe Coinsquare Wallet app requires you to set a PIN to secure your wallet, separate from your device passcode.\n\t\tYou will be required to enter this PIN to view your balance or send money, which keeps your wallet private even if you let someone use your phone or if your phone is stolen by someone who knows your device passcode.\n\t\tDo not forget your wallet PIN! It can only be reset by using your Recovery Key. If you forget your PIN and lose your Recovery Key, your wallet will be lost."),
    RECOVERY_KEY("What is a recovery key?","A recovery key consists of 12 randomly generated words. The app creates the recovery key for you automatically when you start a new wallet. The recovery key is critically important and should be written down and stored in a safe location. In the event of phone theft, destruction, or loss, the recovery key can be used to load your wallet onto a new phone. The key is also required when upgrading your current phone to a new one."),
    WALLET_DISABLED("Why is my wallet disabled?","If you enter an incorrect wallet PIN too many times, your wallet will become disabled for a certain amount of time. This is to prevent someone else from trying to guess your PIN by quickly making many guesses. If your wallet is disabled, wait until the time shown and you will be able to enter your PIN again.\n" +
            "\n" +
            "If you continue to enter the incorrect PIN, the amount of time you will have to wait in between attempts will increase. Eventually, the app will reset and you can start a new wallet.\n" +
            "\n" +
            "If you have the recovery key for your wallet, you can use it to reset your PIN by choosing the appropriate option on the lock screen. ");

    override fun title(): String {
        return this.title
    }

    override fun description(): String {
        return this.description
    }

    companion object {
        fun pages(): Array<BaseSupportPage> {
            return values() as Array<BaseSupportPage>
        }
    }
}

enum class SecurityPage(private val title: String, private val description: String) : BaseSupportPage {
    WHY_WRITE_DOWN_RECOVERY("Why do I need to write down my recovery key?","The Coinsquare Wallet App connects directly to the cryptocurrency networks, where your wallets are stored. The \"keys\" to access your wallets are securely stored in your phone and nowhere else. The Coinsquare Wallet app will provide you with a unique \"Recovery Key,\" which is a list of 12-words which can be used to recreate the keys to your wallet. If you ever lose or upgrade your phone, this \"Recovery Key\" is required to regain access to your funds.\n" +
            "\n" +
            "Please write down each word, in order, on a piece of paper. We recommend storing your Recovery Key in a safe, safety deposit box at a bank, or wherever you might keep passports, birth certificates, or other important documents. Anyone who has your Recovery Key can access your money, even if they don't have your wallet PIN, so keep it private and don't show it to others! "),
    HOW_TO_RESET("How do I reset my PIN with my recovery key?","Every time you create a new wallet with Coinsquare, you are provided with a \"Recovery Key,\" which is a list of 12 words that can be used to restore access to your wallet if you ever lose or upgrade your phone. You can also use your Recovery Key to reset your wallet PIN if you forget it.\n" +
            "\n" +
            "If you have incorrectly entered your PIN several times, you will be able to reset your PIN using your recovery key. Enter the 12 words from your Recovery Key in the correct order, and you will be able to choose a new PIN."),
    HOW_TO_CONFIRM("How do I confirm my recovery key?", "It is very important that your Recovery Key is written down correctly, and in the correct order. Enter the words requested to make sure everything is correct. For example, \"Word #1\" means the first word of your Recovery Key, \"Word #2\" means the second word, etc. "),
    FINGERPRINT("What is fingerprint authentication?","Many Android devices feature a fingerprint reader. You can use your fingerprint to unlock your Coinsquare wallet and authorize transactions instead of entering your passcode. You will still be required to enter your passcode periodically for increased security.\n" +
            "\n" +
            "In order to use this feature in the Coinsquare Wallet app, fingerprint authorization must be enabled on your device. See the phone manufacturer's manual for more information.");

    override fun title(): String {
        return this.title
    }

    override fun description(): String {
        return this.description
    }

    companion object {
        fun pages(): Array<BaseSupportPage> {
            return values() as Array<BaseSupportPage>
        }
    }
}

enum class SettingPage(private val title: String, private val description: String) : BaseSupportPage {
    HOW_WIPE_WALLET("How do I wipe my wallet?","The Coinsquare Wallet App can only have one wallet active at a time. If you choose to start a new wallet or recover an existing wallet, the currently active wallet must be erased first.\n" +
            "\n" +
            "IMPORTANT: If you erase your wallet without writing down its Recovery Key, your money will be lost. To prevent this, you must first enter your Recovery Key for the currently active wallet to make sure you have it written down correctly.\n" +
            "\n" +
            "Follow these steps to wipe a wallet:\n" +
            "\n" +
            "\t\t\t 1 - Tap on Menu in the main screen.\n" +
            "\t\t\t 2 - Select Security Settings.\n" +
            "\t\t\t 3 - Tap on Unlink from this device and then on Next.\n" +
            "\t\t\t 4 - Enter the current wallet’s Recovery Key.\n" +
            "\t\t\t 5 - Tap on Wipe.\n" +
            "\n" +
            "The wallet will then be erased, and you can create a new wallet or recover a previous wallet. "),
    HOW_RECOVER_WALLET("How do I recover a previous wallet I had?","When you launch the Coinsquare Wallet app for the first time, you will be given the option to create a new wallet or to recover a wallet. If you have a 12 word recovery key from a previous Coinsquare wallet that you would like to recover, follow these steps to recover your wallet:\n" +
            "\n" +
            "\t\t\t 1 - Select Restore Wallet.\n" +
            "\t\t\t 2 - Click Next and enter the 12 word Recovery Key in the correct order for the wallet you want to recover.\n" +
            "\t\t\t 3 - Set a PIN.\n" +
            "\t\t\t 4 - Confirm the PIN.\n" +
            "\t\t\t 5 - Leave the app open and on the screen to allow the wallets to sync to the blockchain.\n" +
            "\n" +
            "Initial syncing can take up to 45 minutes or more so please prepare to leave the app open with the screen on for an extended period of time.\n" +
            "\n" +
            "If you wish to recover a wallet using a recovery key but you already have an active wallet loaded in the Coinsquare Wallet app, you will need to follow the directions to wipe the wallet before beginning the process above. If the wallet you are wiping has funds, be sure to write down the 12 word recovery key for the existing wallet (which can be found in the Security Center) before loading the recovery key for the wallet you wish to recover. Visit this page for help on wiping a wallet. "),
    HOW_CHANGE_CURRENCY("How do I change the local currency displayed in my wallet?","Your Coinsquare wallet will show your cryptocurrency and its equivalent fiat value in your local currency by default. \n" +
            "\n" +
            "You can change this to show the value in a different local currency by following these steps:\n" +
            "\n" +
            "\t\t\t 1 - On the main screen, tap Menu.\n" +
            "\t\t\t 2 - Select Preferences and then Display Currency.\n" +
            "\t\t\t 3 - Choose the currency you would like to use from the list.\n"),
    HOW_BALANCE_WORKS("How does the Coinsquare Bitcoin wallet show my balance in my local currency?","Money kept in your Coinsquare Bitcoin wallet is always stored as bitcoin. The app uses an exchange rate to show you an estimate of how much your bitcoin is worth in your local currency. When you send a transaction and enter the amount to send in your local currency, the app uses the exchange rate to calculate how much bitcoin to send.\n" +
            "\n" +
            "It's possible people using other wallets are using different exchange rates, so the balance sent/received might be slight different than what other person sees.\n" +
            "\n" +
            "Since the exchange rate is always changing, the amount of money in your wallet as quoted in your local currency may change over time. However, your bitcoin balance will not change unless you send or receive bitcoin. "),
    HOW_SELECT_NODE("How do I connect to a specific bitcoin node?","The BRD app connects to a random node on the bitcoin network when syncing your wallet. You can choose to connect to a specific node of your preference by following the steps below:\n" +
            "\n" +
            "\t\t\t 1 - On the main screen, tap on Menu.\n" +
            "\t\t\t 2 - Select Preferences and then Bitcoin Settings.\n" +
            "\t\t\t 3 - Choose Bitcoin Nodes\n" +
            "\t\t\t 4 - Tap Switch to Manual Mode\n" +
            "\t\t\t 5 - Enter the Node IP address in the window that pops up.  You may also enter a port, although this is optional.  Tap OK when done.\n" +
            "\n" +
            "Your wallet will now sync to the blockchain via your preferred node.");

    override fun title(): String {
        return this.title
    }

    override fun description(): String {
        return this.description
    }

    companion object {
        fun pages(): Array<BaseSupportPage> {
            return values() as Array<BaseSupportPage>
        }
    }
}
