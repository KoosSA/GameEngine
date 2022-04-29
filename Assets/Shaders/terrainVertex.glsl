#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 normal;

out vec2 passTexCoord;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {

	vec4 worldPosition = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1);

	passTexCoord = textureCoord * 150.0;

	gl_Position = worldPosition;
}
