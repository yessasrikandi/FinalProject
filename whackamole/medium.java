/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package game;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author BLACK
 */
public class medium extends javax.swing.JFrame {
private int level = 1;
private int waktu = 30;
private int score = 0;
private javax.swing.Timer gameTimer;
private javax.swing.Timer moleTimer;
private javax.swing.JButton[] tombolMole;
private int moleIndex = -1;
private int highScore = 0;
    private boolean isBombActive = false;
private int bombIndex = -1;
    private Clip clip;
    
    public medium() {
                 setTitle("GAME");
        initComponents();
            tombolMole = new javax.swing.JButton[]{btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};
             playLoopingMusic();
                       setCustomCursor();  
        setLocationRelativeTo(null);
    
    }
    
    
    private void playLoopingMusic() {
        try {
            // Load musik dari resource di folder game
            InputStream audioSrc = getClass().getResourceAsStream("/game/music.wav");
            if (audioSrc == null) {
                System.err.println("File musik tidak ditemukan!");
                return;
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // loop terus menerus
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void playSoundEffect(String soundFile) {
    try {
        InputStream audioSrc = getClass().getResourceAsStream("/game/" + soundFile);
        if (audioSrc == null) {
            System.err.println("File sound effect tidak ditemukan: " + soundFile);
            return;
        }
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

        Clip clipEffect = AudioSystem.getClip();
        clipEffect.open(audioStream);
        clipEffect.start();
        clipEffect.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clipEffect.close();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @Override
    public void dispose() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
        super.dispose();}
    
    private void setCustomCursor() {
    try {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(getClass().getResource("/game/palu.png"));
        Point hotspot = new Point(0, 0);
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, hotspot, "Custom Cursor");
        this.setCursor(customCursor);
    } catch (Exception e) {
        System.err.println("Gagal load kursor custom: " + e.getMessage());
    }
}

    
private void startGame() {
    waktu = 30;
    score = 0;
    labelScore.setText("SCORE : " + score);
    labelWaktu.setText("TIME : " + waktu);
    
    // Jika timer sebelumnya masih jalan, hentikan dulu
    if (gameTimer != null && gameTimer.isRunning()) {
        gameTimer.stop();
    }
    if (moleTimer != null && moleTimer.isRunning()) {
        moleTimer.stop();
    }
    gameTimer = new javax.swing.Timer(1000, e -> {
        waktu--;
        labelWaktu.setText("TIME : " + waktu);
        if (waktu <= 0) {
            gameTimer.stop();
            moleTimer.stop();
            tampilkanGameOver();
        }
    });
    
    int delay = 1000; 

    
    moleTimer = new javax.swing.Timer(delay, e -> {
        randomMole();
    });
    
    gameTimer.start();
    moleTimer.start();
}
    private void randomMole() {

    if (moleIndex != -1) {
        tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg")));
        moleIndex = -1;
    }
    if (bombIndex != -1) {
        tombolMole[bombIndex].setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg")));
        bombIndex = -1;
        isBombActive = false;
    }

   
    boolean showBomb = Math.random() < 0.1; // 20% kemungkinan muncul bom
    int index = (int) (Math.random() * 9);

    if (showBomb) {
        bombIndex = index;
        isBombActive = true;
        java.net.URL bombURL = getClass().getResource("/game/bom.jpg"); // ← Ubah ke .jpg
        if (bombURL != null) {
            tombolMole[bombIndex].setIcon(new javax.swing.ImageIcon(bombURL));
        } else {
            System.out.println("Gambar bom tidak ditemukan!");
        }
    } else {
        moleIndex = index;
        java.net.URL moleURL = getClass().getResource("/game/si.jpg"); // Mole
        if (moleURL != null) {
            tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(moleURL));
        } else {
            System.out.println("Gambar mole tidak ditemukan!");
        }
    }
}
    private void tampilkanGameOver() {
            if (score > highScore) {
        highScore = score;
           highscore.setText("HIGH SCORE : " + highScore);
    }
 Object[] options = {"Restart", "Back"};
int choice = JOptionPane.showOptionDialog(
    this,
      "Times up!\nScore: " + score + "\nRestart level?",
    "Game Over",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.INFORMATION_MESSAGE,
    null,
    options,
    options[1]
);

if (choice == JOptionPane.YES_OPTION) {
    // User pilih "Restart"
    // Contoh: buka ulang level
   startGame();
} else if (choice == JOptionPane.NO_OPTION) {
    satu nextLevel = new satu();
            nextLevel.setVisible(true);

            // Tutup frame level sekarang
            this.dispose();}
}
    private void whackMole(int index) {
    if (index == bombIndex && isBombActive) {
    playSoundEffect("bomm.wav"); // suara bom
    gameTimer.stop();
    moleTimer.stop();
    JOptionPane.showMessageDialog(this, "Kamu menekan BOM!\nGame Over.", "Game Over", JOptionPane.ERROR_MESSAGE);
    tampilkanGameOver();
    
    return;
}
    
    if (index == moleIndex) {
        playSoundEffect("hit.wav"); 
        score++;
        labelScore.setText("SCORE : " + score);
        tombolMole[moleIndex].setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg")));
        moleIndex = -1;
    } 
    // Kalau bukan mole atau bom (salah klik)
    else {
        playSoundEffect("miss.wav");
    }
}
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        judul = new javax.swing.JLabel();
        labelScore = new javax.swing.JLabel();
        labelWaktu = new javax.swing.JLabel();
        quit4 = new javax.swing.JButton();
        mulai = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        highscore = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setLayout(new java.awt.GridLayout(3, 3));

        btn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn1MouseClicked(evt);
            }
        });
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });
        jPanel3.add(btn1);

        btn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn2MouseClicked(evt);
            }
        });
        jPanel3.add(btn2);

        btn3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn3MouseClicked(evt);
            }
        });
        jPanel3.add(btn3);

        btn4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn4MouseClicked(evt);
            }
        });
        jPanel3.add(btn4);

        btn5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn5MouseClicked(evt);
            }
        });
        jPanel3.add(btn5);

        btn6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn6MouseClicked(evt);
            }
        });
        jPanel3.add(btn6);

        btn7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn7MouseClicked(evt);
            }
        });
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });
        jPanel3.add(btn7);

        btn8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn8MouseClicked(evt);
            }
        });
        jPanel3.add(btn8);

        btn9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/game/sa.jpg"))); // NOI18N
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });
        jPanel3.add(btn9);

        judul.setFont(new java.awt.Font("Sketch 3D", 1, 40)); // NOI18N
        judul.setForeground(new java.awt.Color(0, 0, 0));
        judul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judul.setText("Medium");
        judul.setToolTipText("");
        judul.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        labelScore.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        labelScore.setForeground(new java.awt.Color(0, 0, 0));
        labelScore.setText("SCORE : 0");
        labelScore.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelWaktu.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        labelWaktu.setForeground(new java.awt.Color(0, 0, 0));
        labelWaktu.setText("TIME : 30");
        labelWaktu.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                labelWaktuAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        quit4.setBackground(new java.awt.Color(105, 142, 253));
        quit4.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        quit4.setText("Back");
        quit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit4ActionPerformed(evt);
            }
        });

        mulai.setBackground(new java.awt.Color(105, 142, 253));
        mulai.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        mulai.setText("Start");
        mulai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mulaiActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Project/bg2.jpg"))); // NOI18N

        highscore.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        highscore.setForeground(new java.awt.Color(0, 0, 0));
        highscore.setText("HIGHSCORE : 0 ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(judul, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(labelScore, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(highscore))
                            .addComponent(quit4))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(141, 141, 141)
                                .addComponent(mulai))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(41, 41, 41))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(50, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(72, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(11, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelScore)
                    .addComponent(labelWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(highscore))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 269, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quit4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mulai, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(111, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(63, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn1MouseClicked
        whackMole(0);        // TODO add your handling code here:
    }//GEN-LAST:event_btn1MouseClicked

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn2MouseClicked
        whackMole(1);        // TODO add your handling code here:
    }//GEN-LAST:event_btn2MouseClicked

    private void btn3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn3MouseClicked
        whackMole(2);        // TODO add your handling code here:
    }//GEN-LAST:event_btn3MouseClicked

    private void btn4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn4MouseClicked
        whackMole(3);        // TODO add your handling code here:
    }//GEN-LAST:event_btn4MouseClicked

    private void btn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn5MouseClicked
        whackMole(4);        // TODO add your handling code here:
    }//GEN-LAST:event_btn5MouseClicked

    private void btn6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn6MouseClicked
        whackMole(5);        // TODO add your handling code here:
    }//GEN-LAST:event_btn6MouseClicked

    private void btn7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn7MouseClicked
        whackMole(6);        // TODO add your handling code here:
    }//GEN-LAST:event_btn7MouseClicked

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn8MouseClicked
        whackMole(7);        // TODO add your handling code here:
    }//GEN-LAST:event_btn8MouseClicked

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
    whackMole(8);           // TODO add your handling code here:
    }//GEN-LAST:event_btn9ActionPerformed

    private void labelWaktuAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_labelWaktuAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_labelWaktuAncestorAdded

    private void quit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit4ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            satu nextLevel = new satu();
            nextLevel.setVisible(true);

            // Tutup frame level sekarang
            this.dispose();
        }
    }//GEN-LAST:event_quit4ActionPerformed

    private void mulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mulaiActionPerformed
        startGame();        // TODO add your handling code here:
    }//GEN-LAST:event_mulaiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(medium.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(medium.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(medium.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(medium.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new medium().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JLabel highscore;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel judul;
    private javax.swing.JLabel labelScore;
    private javax.swing.JLabel labelWaktu;
    private javax.swing.JButton mulai;
    private javax.swing.JButton quit4;
    // End of variables declaration//GEN-END:variables
}
