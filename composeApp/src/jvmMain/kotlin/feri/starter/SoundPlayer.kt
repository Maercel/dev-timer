package feri.starter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import starterdesktopapp.composeapp.generated.resources.Res
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent

object SoundPlayer {

    suspend fun playDone() {
        runCatching {
            val bytes = Res.readBytes("files/session_complete.wav")
            withContext(Dispatchers.IO) {
                val stream = AudioSystem.getAudioInputStream(ByteArrayInputStream(bytes))
                val clip = AudioSystem.getClip()
                clip.addLineListener { event ->
                    if (event.type == LineEvent.Type.STOP) clip.close()
                }
                clip.open(stream)
                clip.start()
            }
        }
    }
}
