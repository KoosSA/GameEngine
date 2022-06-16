#version 460

out vec4 outColour;

uniform vec3 colour;

void main() {

	outColour = vec4(colour, 1);

}
