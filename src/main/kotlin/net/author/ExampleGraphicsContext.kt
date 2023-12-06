package net.author

import net.botwithus.rs3.imgui.ImGui
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.ScriptGraphicsContext

class ExampleGraphicsContext(
    private val script: ExampleScript,
    console: ScriptConsole
) : ScriptGraphicsContext (console) {

    // Static String for the custom text with a default value of "Cow"
    private val defaultToKill = "Cow"
    private var userInputToKill: String = "Cow"
    private var boxInputToKill: String = "please help"

    // Flag to track whether the button has been pressed
    private var saveButtonPressed = false


    override fun drawSettings() {
        super.drawSettings()
        ImGui.Begin("Thing Killer", 0)
        ImGui.SetWindowSize(250f, -1f)
        ImGui.Text("Kill Things")
        if (ImGui.Button("Start")) {
            script.botState = ExampleScript.BotState.KILLING;
            saveButtonPressed = true

        }
        ImGui.SameLine()
        if (ImGui.Button("Stop")) {
            script.botState = ExampleScript.BotState.IDLE

        }
        script.bankPreset.set(ImGui.InputInt("Bank preset", script.bankPreset.get()))
        script.doSomething.set(ImGui.Checkbox("Do something", script.doSomething.get()))


        boxInputToKill = ImGui.InputText("Mob to Kill", userInputToKill)

        // Button to save the input as a variable
        if (ImGui.Button("Change Mob")) {
            ScriptConsole.println("Entered: $boxInputToKill")
            ScriptConsole.println("Prev: $userInputToKill")

            saveButtonPressed = true
            userInputToKill = boxInputToKill
        }
        // Check if the button has been pressed and update the stored value
        if (saveButtonPressed) {
            // Custom text has changed, handle accordingly
            // Update the script's mobToKill variable with the entered custom text

            ScriptConsole.println("change mob from "+ script.mobToKill+ " to "+ userInputToKill)
            script.mobToKill = userInputToKill
            ScriptConsole.println("change mob to "+ script.mobToKill)
            // Reset the flag
            saveButtonPressed = false
        }
        ImGui.End()
    }

    override fun drawOverlay() {
        super.drawOverlay()
    }

}