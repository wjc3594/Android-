package com.example.openglapplication

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        init {
            setEGLContextClientVersion(2)
            render = MyGLRender()
            setRenderer(render)
            renderMode = RENDERMODE_WHEN_DIRTY
        }
    }

    class MyGLRender : GLSurfaceView.Renderer {
        private lateinit var mTriangle: Triangle
        private lateinit var mSquare: Square2
        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            mTriangle = Triangle()
            mSquare = Square2()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            mTriangle.draw()
        }


    }

    class Triangle {
        private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}"

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

        fun draw() {
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