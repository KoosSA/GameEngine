#version 460

layout (location = 0) in vec3 position;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {

	vec4 worldPosition = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1);

	gl_Position = worldPosition;
}
