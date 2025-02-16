package com.example.openglapplication

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

const val COORDS_PER_VERTX = 3

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MyGLSurfaceView(this)
        setContentView(glView)
    }

    private lateinit var glView: GLSurfaceView

    class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
        private val render: MyGLRender
        private val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
        private var previousX: Float = 0f
        private var previousY: Float = 0f

        init {
            setEGLContextClientVersion(2)
            render = MyGLRender()
            setRenderer(render)
//            renderMode = RENDERMODE_WHEN_DIRTY
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            event?.let {
                val x: Float = it.x
                val y: Float = it.y
                when (it.action) {
                    MotionEvent.ACTION_MOVE -> {
                        var dx: Float = x - previousX
                        var dy: Float = y - previousY
                        if (y > height / 2) {
                            dx *= -1
                        }
                        if (x < width / 2) {
                            dy *= -1
                        }
                        render.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                        requestRender()
                    }
                }
                previousX = x
                previousY = y
                return true
            }
            return false
        }
    }

    class MyGLRender : GLSurfaceView.Renderer {
        private lateinit var mTriangle: Triangle
        private lateinit var mSquare: Square2
        private val vPMatrix = FloatArray(16)
        private val projectionMatrix = FloatArray(16)
        private val viewMatrix = FloatArray(16)
        private val rotationMatrix = FloatArray(16)

        @Volatile
        var angle: Float = 0f
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            mTriangle = Triangle()
            mSquare = Square2()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            val ratio = width.toFloat() / height.toFloat()
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        }

        override fun onDrawFrame(gl: GL10?) {
            val scratch = FloatArray(16)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
//            val time = SystemClock.uptimeMillis() % 4000L
//            val angle = 0.090f * time.toInt()
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            mTriangle.draw(scratch)
        }


    }

    /**
     * 定义形状 https://developer.android.google.cn/training/graphics/opengl/shapes
     */
    class Triangle {
        private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}"

        // Use to access and set the view transformation
        private var vPMatrixHandle: Int = 0

        private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}"
        var triangleCoords = floatArrayOf(
            0.0f,
            0.622008459f,
            0.0f,
            -0.5f,
            -0.311004243f,
            0.0f,
            0.5f,
            -0.311004243f,
            0.0f
        )
        val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0
        private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTX
        private val vertexStride: Int = COORDS_PER_VERTX * 4
        private var mProgram: Int

        init {
            val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
            mProgram = GLES20.glCreateProgram().also {
                GLES20.glAttachShader(it, vertexShader)
                GLES20.glAttachShader(it, fragmentShader)
                GLES20.glLinkProgram(it)
            }
        }

        private var vertexBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(triangleCoords)
                    position(0)
                }
            }

        private fun loadShader(trye: Int, shaderCode: String): Int {
            return GLES20.glCreateShader(trye).also { shader ->
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        fun draw(mvpMatrix: FloatArray) {
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
            GLES20.glDisableVertexAttribArray(positionHandle)
            GLES20.glUseProgram(mProgram)
            positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
                GLES20.glEnableVertexAttribArray(it)
                GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )
                mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                }
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
                GLES20.glDisableVertexAttribArray(it)
            }
        }
    }

    class Square2 {
        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
        var squareCoords = floatArrayOf(
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
        )
        private val vertexBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(squareCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(squareCoords)
                    position(0)
                }
            }
        private val drawListBuffer: ShortBuffer =
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }
    }
}