package net.author

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.Bank
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.characters.player.Player
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.imgui.NativeBoolean
import net.botwithus.rs3.imgui.NativeInteger
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.script.config.ScriptConfig
import java.util.Random

class ExampleScript(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript (name, scriptConfig, scriptDefinition) {

    val random: Random = Random()
    var botState: BotState = BotState.IDLE
    var bankPreset: NativeInteger = NativeInteger(1)
    var doSomething: NativeBoolean = NativeBoolean(false)

    enum class BotState {
        IDLE,
        SKILLING,
        BANKING,
        //etc..
    }

    override fun initialize(): Boolean {
        super.initialize()
        // Set the script graphics context to our custom one
        this.sgc = ExampleGraphicsContext(this, console)
        println("My script loaded!")
        return true;
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer();
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null || botState == BotState.IDLE) {
            Execution.delay(random.nextLong(2500,5500))
            return
        }
        when (botState) {
            BotState.SKILLING -> {
                Execution.delay(handleSkilling(player))
                return
            }
            BotState.BANKING -> {
                Execution.delay(handleBanking(player))
                return
            }
            else -> {
                println("Unexpected bot state, report to author!")
            }
        }
        Execution.delay(random.nextLong(2000,4000))
        return
    }

    private fun handleBanking(player: Player): Long {
        if (player.isMoving || player.animationId != -1)
            return random.nextLong(1000,2000)

        if (Bank.isOpen()) {
            //do bank logic
        } else {
            val sceneObject: SceneObject? = SceneObjectQuery.newQuery().name("Bank chest").option("Use").results().nearest()
            sceneObject?.interact("Use")
            botState = BotState.SKILLING;
        }
        return random.nextLong(1000,3000)
    }

    private fun handleSkilling(player: Player): Long {
        if (player.isMoving || player.animationId != -1)
            return random.nextLong(1000,2000)
        //do something
        if (Backpack.isFull()) {
            botState = BotState.BANKING
        } else {
            //do something! chop a tree, click a rock, etc.
            //Interfaces.isOpen(1251) // the progress window that pops up when you're doing a skilling action like making pots

            //Interact with some logs in inventory, 1473 is interface ID for backpack (found by using Minimenu overlay and hovering the log)
            //val component: Component? = ComponentQuery.newQuery(1473).itemName("Logs").results().first()
            //component?.interact("Light")
        }
        return random.nextLong(1000,3000)
    }
}