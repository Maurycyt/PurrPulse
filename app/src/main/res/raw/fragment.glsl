const int n = 30;

varying vec2 v_position;
uniform vec2 u_positions[n];
uniform vec3 u_colors[n];

void main() {
    float min_dist = distance(u_positions[0], v_position);
    int idx = 0;
    for (int i = 1; i <= n; i++) {
        float dist = distance(u_positions[i], v_position);
        if (dist < min_dist) {
            min_dist = dist;
            idx = i;
        }
    }
    gl_FragColor = vec4(u_colors[idx], 1.0);
}